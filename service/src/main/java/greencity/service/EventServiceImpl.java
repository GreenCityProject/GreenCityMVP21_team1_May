package greencity.service;

import greencity.dto.event.AdditionalImageForEventDtoRequest;
import greencity.dto.event.EventDateLocationDtoRequest;
import greencity.dto.event.EventDtoCreateResponse;
import greencity.dto.event.EventDtoRequest;
import greencity.entity.*;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final UserService userService;
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

        return modelMapper.map(event, EventDtoCreateResponse.class);
    }

    @Override
    public EventDtoCreateResponse getBtId(Long id) {
        var eventOptional = eventRepo.findById(id);
        var event = eventOptional.orElseThrow();
        return modelMapper.map(event, EventDtoCreateResponse.class);
    }
}
