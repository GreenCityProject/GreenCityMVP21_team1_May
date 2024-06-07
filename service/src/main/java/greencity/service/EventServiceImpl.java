package greencity.service;

import greencity.client.RestClient;
import greencity.dto.event.AdditionalImageForEventDtoRequest;
import greencity.dto.event.EventDateLocationDtoRequest;
import greencity.dto.event.EventDtoCreateResponse;
import greencity.dto.event.EventDtoRequest;
import greencity.dto.user.NotificationDto;
import greencity.entity.*;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public EventDtoCreateResponse create(EventDtoRequest dto, String principal) {
        var organiser = userService.findByEmail(principal);

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .titleImage(dto.getTitleImage())
                .isOpen(dto.getIsOpen())
                .organizer(User.builder()
                        .id(organiser.getId())
                        .build())
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

        List<AdditionalImage> additionalImageList = new ArrayList<>();
        for (AdditionalImageForEventDtoRequest e : dto.getAdditionalImages()) {
            AdditionalImage additionalImage = modelMapper.map(e, AdditionalImage.class);

            additionalImage.setEvent(event);

            additionalImageList.add(additionalImage);
        }
        event.setAdditionalImages(additionalImageList);
        event = eventRepo.save(event);

        NotificationDto notificationDto = prepareNotificationFromEvent(event);

        restClient.sendNotificationToUser(notificationDto, organiser.getEmail());

        return modelMapper.map(event, EventDtoCreateResponse.class);
    }

    private NotificationDto prepareNotificationFromEvent(Event event) {
        String datesTable = "<table>" +
                "<thead>" +
                "<tr>" +
                "<th>Start Time</th>" +
                "<th>End Time</th>" +
                "<th>Address</th>" +
                "<th>Online Link</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>" +
                event.getDates().stream()
                        .map(eventDate -> String.format("<tr>" +
                                        "<td>%s</td>" +
                                        "<td>%s</td>" +
                                        "<td>%s</td>" +
                                        "<td><a href=\"%s\">Link</a></td>" +
                                        "</tr>",
                                eventDate.getStartTime(),
                                eventDate.getEndTime(),
                                eventDate.getAddress().getFormattedAddressUa(),
                                eventDate.getOnlineLink()))
                        .collect(Collectors.joining()) +
                "</tbody></table>";

        String emailContent = String.format("<html>" +
                        "<head>" +
                        "<style>" +
                        "table { width: 100%%; border-collapse: collapse; }" +
                        "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                        "th { background-color: #f2f2f2; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1>%s</h1>" +
                        "<p>%s</p>" +
                        "%s" +
                        "</body>" +
                        "</html>",
                event.getTitle(),
                event.getDescription(),
                datesTable);

        return NotificationDto.builder()
                .body(emailContent)
                .title(String.format("Event \"%s...\" created", event.getTitle().substring(0, 15)))
                .build();
    }
}
