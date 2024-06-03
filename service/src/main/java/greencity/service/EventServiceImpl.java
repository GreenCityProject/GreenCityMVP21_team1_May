package greencity.service;

import greencity.dto.event.*;
import greencity.dto.user.UserForEventDtoResponse;
import greencity.entity.AdditionalImage;
import greencity.entity.Address;
import greencity.entity.Event;
import greencity.entity.EventDateLocation;
import greencity.repository.AdditionalImageRepo;
import greencity.repository.AddressRepo;
import greencity.repository.EventDateLocationRepo;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final AdditionalImageRepo additionalImageRepo;
    private final EventDateLocationRepo eventDateLocationRepo;
    private final AddressRepo addressRepo;
    private final UserService userService;

    @Override
    public EventDtoCreateResponse create(EventDtoRequest dto, String principal) {
        var organiser = userService.findByEmail(principal);

        Event event = eventRepo.save(Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .titleImage(dto.getTitleImage())
                .isOpen(dto.getIsOpen())
                .build());
        List<EventDateLocation> eventDateLocationList = new ArrayList<>();

        for (EventDateLocationDtoResponse e : dto.getDates()) {
            Address address = addressRepo.save(Address.builder()
                    .latitude(e.getAddress().getLatitude())
                    .longitude(e.getAddress().getLongitude())
//                    .eventDateLocation(eventDateLocation)
                    .build());

            EventDateLocation eventDateLocation = eventDateLocationRepo.save(EventDateLocation.builder()
                    .startTime(e.getStartTime())
                    .endTime(e.getEndTime())
                    .onlineLink(e.getOnlineLink())
                    .build());

            address.setEventDateLocation(eventDateLocation);
            addressRepo.save(address);
            eventDateLocation.setAddress(address);
            eventDateLocationList.add(eventDateLocationRepo.save(eventDateLocation));
        }
        event.setDates(eventDateLocationList);

        List<AdditionalImage> additionalImageList = new ArrayList<>();
        for (AdditionalImageForEventDtoResponse e : dto.getAdditionalImages()) {
            AdditionalImage additionalImage = AdditionalImage.builder()
                    .data(e.getData())
                    .event(event)
                    .build();
            additionalImageList.add(additionalImageRepo.save(additionalImage));
        }
        event.setAdditionalImages(additionalImageList);

        event = eventRepo.save(event);

        return EventDtoCreateResponse.builder()
                .id(event.getId())
                .timestamp(event.getTimestamp())
                .title(event.getTitle())
                .dates(event.getDates().stream().map(e -> EventDateLocationDtoResponse.builder()
                        .id(e.getId())
                        .eventId(e.getEvent().getId())
                        .startTime(e.getStartTime())
                        .endTime(e.getEndTime())
                        .onlineLink(e.getOnlineLink())
                        .address(AddressDtoResponse.builder()
                                .latitude(e.getAddress().getLatitude())
                                .longitude(e.getAddress().getLongitude())
                                .build())
                        .build()
                ).toList())
                .organizer(UserForEventDtoResponse.builder()
                        .id(event.getOrganizer().getId())
                        .name(event.getOrganizer().getName())
                        .organizerRating(event.getOrganizer().getRating())
                        .email(event.getOrganizer().getEmail())
                        .build())
                .description(event.getDescription())
                .isOpen(event.getIsOpen())
                .titleImage(event.getTitleImage())
                .additionalImages(event.getAdditionalImages().stream().map(ai ->
                                AdditionalImageForEventDtoResponse.builder()
                                        .data(ai.getData())
                                        .build())
                        .toList())
                .organizer(UserForEventDtoResponse.builder()
                        .id(organiser.getId())
                        .name(organiser.getName())
                        .organizerRating(organiser.getRating())
                        .email(organiser.getEmail())
                        .build())
                .build();
    }
}
