package greencity.service;

import greencity.client.RestClient;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventDateLocationDtoRequest;
import greencity.dto.user.NotificationDto;
import greencity.entity.*;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
    public EventCreateDtoResponse create(EventCreateDtoRequest dto, MultipartFile[] images, String principal) {

        List<Event> fetchedEvents = eventRepo.findByTitle(dto.getTitle());
        checkIfEventExistsOrElseThrow(dto, fetchedEvents);

        var organiser = userService.findByEmail(principal);

        /**
         * If no @images: titleImage set to empty String, additionalImages set to empty List
         * If 1 @images:  this image is set to titleImage, additionalImages set to empty List
         * If >1 @images: first image is set to titleImage, others are set to additionalImages
         */
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

    private static void checkIfEventExistsOrElseThrow(EventCreateDtoRequest dto, List<Event> fetchedEvents) {
        if (!fetchedEvents.isEmpty()) {
            List<EventDateLocation> edl = dto.getDates().stream()
                    .map(x -> EventDateLocation.builder()
                            .address(Address.builder()
                                    .latitude(x.getAddress().getLatitude())
                                    .longitude(x.getAddress().getLongitude())
                                    .build())
                            .startTime(x.getStartTime())
                            .endTime(x.getEndTime())
                            .onlineLink(x.getOnlineLink())
                            .build())
                    .toList();
            Event icomeEvent = Event.builder()
                    .title(dto.getTitle())
                    .dates(edl)
                    .description(dto.getDescription())
                    .build();
            for (Event fatchedEvent : fetchedEvents) {
                if (icomeEvent.equals(fatchedEvent)) {
                    throw new IllegalArgumentException("Event already exist");
                }
            }
        }
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

    private static NotificationDto prepareNotificationFromEvent(Event event) {
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
}
