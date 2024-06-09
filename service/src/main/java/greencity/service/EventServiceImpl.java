package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.event.*;
import greencity.dto.user.NotificationDto;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.enums.Role;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.AdditionalImageRepo;
import greencity.repository.AddressRepo;
import greencity.repository.EventDateLocationRepo;
import greencity.repository.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final EventDateLocationRepo eventDateLocationRepo;
    private final AdditionalImageRepo additionalImageRepo;
    private final AddressRepo addressRepo;
    private final UserService userService;
    private final RestClient restClient;
    private final FileService fileService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public EventCreateDtoResponse create(EventCreateDtoRequest dto, MultipartFile[] images, String principal) {

        var organiser = userService.findByEmail(principal);

        String titleImageLink = getTitleImageLinkOrDie(images);

        var additionalImagesLinks = Arrays.stream(images)
                .map(fileService::upload)
                .map(data -> AdditionalImage.builder().data(data).build())
                .toList();

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .titleImage(titleImageLink)
                .isOpen(dto.getIsOpen())
                .organizer(User.builder()
                        .id(organiser.getId())
                        .build())
                .additionalImages(additionalImagesLinks)
                .build();

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

        if (user.getRole() != Role.ROLE_ADMIN || !user.getId().equals(event.getOrganizer().getId())) {
            throw new BadRequestException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }

        List<EventDateLocation> eventDateLocationList = new ArrayList<>();
        for (EventDateLocationDtoRequest e : eventUpdate.getDates()) {
            EventDateLocation eventDateLocation = modelMapper.map(e, EventDateLocation.class);
            Address address = modelMapper.map(e.getAddress(), Address.class);

            eventDateLocation.setAddress(address);
            eventDateLocation.setEvent(event);

            eventDateLocationList.add(eventDateLocation);
        }

        List<AdditionalImage> additionalImagesLinks = Arrays.stream(images)
                .map(fileService::upload)
                .map(data -> AdditionalImage.builder().data(data).build())
                .toList();

        event.setTitle(eventUpdate.getTitle());
        event.setDescription(eventUpdate.getDescription());
        event.setDates(eventDateLocationList);
        event.setTitleImage(getTitleImageLinkOrDie(images));
        event.setAdditionalImages(additionalImagesLinks);

        //todo: check images delete

        eventRepo.save(event);

        return modelMapper.map(event, EventCreateDtoResponse.class);
    }

    private String getTitleImageLinkOrDie(MultipartFile[] images) {
        if (images.length != 0 && !images[0].isEmpty()) {
            return fileService.upload(images[0]);
        }

        File file = new File("service/tempImage.png");
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            log.warn("No default image for Event Tile was found! Title image now set to NULL.");
            return "";
        }

        MultipartFile multipartFile = new MultipartFileImpl("name", "tempImage.png", "multipart/form-data", bytes);

        return fileService.upload(multipartFile);
    }

    private NotificationDto prepareNotificationFromEvent(Event event) {
        String datesTable =
                "        <table>                            " +
                        "       <thead>                     " +
                        "           <tr>                    " +
                        "               <th>Start Time</th> " +
                        "                 <th>End Time</th> " +
                        "                 <th>Address</th>  " +
                        "               <th>Online Link</th>" +
                        "           </tr>                   " +
                        "       </thead>                    " +
                        "   <tbody>                         " +
                        event.getDates().stream()
                                .map(eventDate -> String.format(
                                        "        <tr>                               " +
                                                "   <td>%s</td>                     " +
                                                "   <td>%s</td>                     " +
                                                "   <td>%s</td>                     " +
                                                "   <td><a href=\"%s\">Link</a></td>" +
                                                "</tr>                              ",
                                        eventDate.getStartTime(),
                                        eventDate.getEndTime(),
                                        eventDate.getAddress().getFormattedAddressUa(),
                                        eventDate.getOnlineLink()))
                                .collect(Collectors.joining()) +
                        "   </tbody>                        " +
                        "</table>                           ";

        String emailContent = String.format(
                "        <html>                                                                        " +
                        "   <head>                                                                     " +
                        "      <style>                                                                 " +
                        "          table { width: 100%%; border-collapse: collapse; }                  " +
                        "          th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }  " +
                        "          th { background-color: #f2f2f2; }                                   " +
                        "      </style>                                                                " +
                        "   </head>                                                                    " +
                        "      <body>                                                                  " +
                        "          <h1>%s</h1>                                                         " +
                        "           <p>%s</p>                                                          " +
                        "              %s                                                              " +
                        "      </body>                                                                 " +
                        "</html>                                                                       ",
                event.getTitle(),
                event.getDescription(),
                datesTable);

        return NotificationDto.builder()
                .body(emailContent)
                .title(String.format("Event \"%s...\" created", event.getTitle().substring(0, 15)))
                .build();
    }
}
