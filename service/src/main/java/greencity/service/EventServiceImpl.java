package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventDateLocationDtoRequest;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.NotificationDto;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.enums.Role;
import greencity.exception.exceptions.AlreadyExistException;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EventRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final UserService userService;
    private final RestClient restClient;
    private final FileService fileService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public EventCreateDtoResponse create(EventCreateDtoRequest dto, MultipartFile[] images, String principal) {

        List<Event> fetchedEvents = eventRepo.findByTitle(dto.getTitle());
        checkIfEventExistsOrElseThrow(dto, fetchedEvents);

        var organiser = userService.findByEmail(principal);
        var additionalImagesLinks = getLinksOfImages(images);
        var titleImage = additionalImagesLinks.isEmpty() ? "" : additionalImagesLinks.getFirst().getData();
        var additionalImageList = additionalImagesLinks.size() > 1 ?
                additionalImagesLinks.subList(1, additionalImagesLinks.size()) : new ArrayList<AdditionalImage>();

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .titleImage(titleImage)
                .isOpen(dto.getIsOpen())
                .organizer(User.builder()
                        .id(organiser.getId())
                        .name(organiser.getName())
                        .rating(organiser.getRating())
                        .email(organiser.getEmail())
                        .build())
                .build();
        for (AdditionalImage a : additionalImageList) {
            a.setEvent(event);
        }
        event.setAdditionalImages(additionalImageList);

        List<EventDateLocation> eventDateLocationList = new ArrayList<>();
        for (EventDateLocationDtoRequest e : dto.getDates()) {
            EventDateLocation eventDateLocation = modelMapper.map(e, EventDateLocation.class);
            Address address = modelMapper.map(e.getAddress(), Address.class);

            eventDateLocation.setAddress(address);
            eventDateLocation.setEvent(event);

            eventDateLocationList.add(eventDateLocation);
        }
        event.setDates(eventDateLocationList);
        event = eventRepo.save(event);

        restClient.sendNotificationToUser(prepareNotificationFromEvent(event), organiser.getEmail());

        return modelMapper.map(event, EventCreateDtoResponse.class);
    }

    @Override
    public List<EventCreateDtoResponse> getAll() {
        List<Event> events = this.eventRepo.findAll();
        return events.stream()
                .map(e -> modelMapper.map(e, EventCreateDtoResponse.class))
                .toList();
    }

    @Override
    public EventCreateDtoResponse findEventById(Long id) {
        Optional<Event> event = eventRepo.findById(id);
        if(event.isPresent()) {
            return modelMapper.map(event.get(), EventCreateDtoResponse.class);
        }
        else throw new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + id);
    }

    @Override
    @Transactional
    public EventCreateDtoResponse update(EventUpdateDtoRequest eventUpdate, MultipartFile[] images, UserVO user){
        Long eventId = eventUpdate.getId();
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));

        if (user.getRole() != Role.ROLE_ADMIN && !Objects.equals(user.getId(), event.getOrganizer().getId())) {
            throw new BadRequestException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }

        List<EventDateLocation> eventDateLocationList = convertLocationList(eventUpdate, event);

        List<AdditionalImage> additionalImagesLinks = getLinksOfImages(images);
        String titleImage = additionalImagesLinks.isEmpty() ? "" : additionalImagesLinks.getFirst().getData();
        List<AdditionalImage> additionalImageList = additionalImagesLinks.size() > 1 ?
                additionalImagesLinks.subList(1, additionalImagesLinks.size()) : new ArrayList<>();

        for(AdditionalImage a: additionalImageList){
            a.setEvent(event);
        }

        event.setTitle(eventUpdate.getTitle());
        event.setDescription(eventUpdate.getDescription());
        event.setDates(eventDateLocationList);
        event.setTitleImage(titleImage);
        event.setAdditionalImages(additionalImageList);

        eventRepo.save(event);

        return modelMapper.map(event, EventCreateDtoResponse.class);
    }

    @Override
    @Transactional
    public PageableAdvancedDto<EventCreateDtoResponse> findEventByQuery(String query, Pageable pageable) {
        List<String> words = Arrays.stream(query.split(" ")).toList();
        List<Event> events = eventRepo.findAll(prepareWhereConditions(words));

        var l1 = events.stream().map(Event::getId).toList().toString();

        List<EventCreateDtoResponse> sortedEvents = events.stream()
                .sorted((e1, e2) -> {
                    long count1 = words.stream()
                            .mapToLong(word -> countOccurrences(e1.getTitle().toLowerCase(), word.toLowerCase()))
                            .sum();
                    long count2 = words.stream()
                            .mapToLong(word -> countOccurrences(e2.getTitle().toLowerCase(), word.toLowerCase()))
                            .sum();
                    return Long.compare(count2, count1); // descending order
                })
                .map(event -> modelMapper.map(event, EventCreateDtoResponse.class))
                .toList();

        log.error(l1);
        log.error(sortedEvents.stream().map(EventCreateDtoResponse::getId).toList().toString());


        long offset = (long) pageable.getPageNumber() * pageable.getPageSize();
        List<EventCreateDtoResponse> page = sortedEvents.stream()
                .skip(offset)
                .limit(pageable.getPageSize())
                .toList();
        long totalElements = events.size();
        int currentPage = pageable.getPageNumber();// from 0 to last
        int totalPages = (int) Math.ceil((double) (events.size()) / pageable.getPageSize());//amount of pages
        int number = pageable.getPageNumber();//same as currentPage
        boolean hasPrevious = number != 0;
        boolean hasNext = number < totalPages - 1;
        boolean first = number == 0;
        boolean last = number == totalPages - 1;

        return new PageableAdvancedDto<>(
                page,
                totalElements,
                currentPage,
                totalPages,
                number,
                hasPrevious,
                hasNext,
                first,
                last);
    }

    private long countOccurrences(String title, String word) {
        int count = 0;
        int idx = 0;
        while ((idx = title.indexOf(word, idx)) != -1) {
            count++;
            idx += word.length();
        }
        return count;
    }
    private Specification<Event> prepareWhereConditions(List<String> words) {
        return (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[words.size()];
            for (int i = 0; i < words.size(); i++) {
                predicates[i] = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + words.get(i).toLowerCase() + "%");
            }
            return criteriaBuilder.and(predicates);
        };
    }

    private void checkIfEventExistsOrElseThrow(EventCreateDtoRequest dto, List<Event> fetchedEvents) {
        if (fetchedEvents.isEmpty()) return;

        Event icomeEvent = modelMapper.map(dto, Event.class);

        for (Event fatchedEvent : fetchedEvents) {
            if (icomeEvent.equals(fatchedEvent)) {
                throw new AlreadyExistException(String.format("Event with title '%s' already exist", dto.getTitle()));
            }
        }
    }

    List<EventDateLocation> convertLocationList(EventUpdateDtoRequest eventUpdate, Event event){
        List<EventDateLocation> eventDateLocationList = new ArrayList<>();
        for (EventDateLocationDtoRequest e : eventUpdate.getDates()) {
            EventDateLocation eventDateLocation = modelMapper.map(e, EventDateLocation.class);
            Address address = modelMapper.map(e.getAddress(), Address.class);

            eventDateLocation.setAddress(address);
            eventDateLocation.setEvent(event);

            eventDateLocationList.add(eventDateLocation);
        }
        return eventDateLocationList;
    }

    private List<AdditionalImage> getLinksOfImages(MultipartFile[] images) {
        if (images.length == 0) {
            File file = new File("service/tempImage.png");
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                log.warn("No default image for Event Tile was found! Title image now set to NULL.");
                return new ArrayList<>();
            }

            MultipartFile multipartFile = new MultipartFileImpl("name", "tempImage.png", "multipart/form-data", bytes);

            AdditionalImage defaultImage = AdditionalImage.builder().data(fileService.upload(multipartFile)).build();
            return List.of(defaultImage);
        }
        List<String> list = new ArrayList<>();
        for (MultipartFile mpf : images) {
            list.add(fileService.upload(mpf));
        }
        return list.stream().map(imLink -> AdditionalImage.builder()
                        .data(imLink)
                        .build())
                .toList();
    }

    private NotificationDto prepareNotificationFromEvent(Event event) {
        String datesTable = """
                <table>
                    <thead>
                        <tr>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th>Address</th>
                            <th>Online Link</th>
                        </tr>
                    </thead>
                    <tbody>
                """ +
                event.getDates().stream()
                        .map(eventDate -> String.format("""
                                        <tr>
                                           <td>%s</td>
                                           <td>%s</td>
                                           <td>%s</td>
                                           <td><a href=\\"%s\\">Link</a></td>
                                        </tr>
                                        """,
                                eventDate.getStartTime(),
                                eventDate.getEndTime(),
                                eventDate.getAddress().getFormattedAddressUa(),
                                eventDate.getOnlineLink()))
                        .collect(Collectors.joining()) +
                "   </tbody>" +
                "</table>";
        String emailContent = String.format("""
                        <html>
                           <head>
                              <style>
                                  table { width: 100%%; border-collapse: collapse; }
                                  th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                                  th { background-color: #f2f2f2; }
                              </style>
                           </head>
                              <body>
                                  <h1>%s</h1>
                                   <p>%s</p>
                                      %s
                              </body>
                        </html>
                        """,
                event.getTitle(),
                event.getDescription(),
                datesTable);

        return NotificationDto.builder()
                .body(emailContent)
                .title(String.format("Event \"%s...\" created", event.getTitle()))
                .build();
    }

    /**
     * Deletes an event with the specified eventId and name.
     *
     * @param eventId The ID of the event to be deleted.
     * @param email The email of the user who is deleting the event.
     */
    @Override
    public void delete(Long eventId, String email) {
        UserVO userVO = userService.findByEmail(email);
        Event toDelete = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND));

        if (toDelete.getOrganizer().getId().equals(userVO.getId()) || userVO.getRole() == Role.ROLE_ADMIN) {
            eventRepo.delete(toDelete);
        } else {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }
    }
}
