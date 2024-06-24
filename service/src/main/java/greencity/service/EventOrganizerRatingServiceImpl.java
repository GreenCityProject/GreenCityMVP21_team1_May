package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingDto;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventDateLocation;
import greencity.entity.EventOrganizerRating;
import greencity.entity.User;
import greencity.exception.exceptions.AlreadyExistException;
import greencity.exception.exceptions.EventIsNotEndedException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.EventOrganizerRatingRepo;
import greencity.repository.EventRepo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventOrganizerRatingServiceImpl implements EventOrganizerRatingService {

    private final EventOrganizerRatingRepo eventOrganizerRatingRepo;
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;

    @Override
    public RantingDto create(Long eventId, RantingCreateDtoRequest rantingCreateDtoRequest,
                             UserVO userVO) {
        Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));
        boolean isEventEnded = event.getDates().stream()
            .map(EventDateLocation::getEndTime)
            .anyMatch(entTime -> LocalDateTime.now().isAfter(entTime));
        if (!isEventEnded) {
            throw new EventIsNotEndedException(ErrorMessage.EVENT_IS_NOT_ENDED + eventId);
        }
        eventOrganizerRatingRepo.findByUserId(userVO.getId())
            .ifPresent(rating -> {throw new AlreadyExistException(ErrorMessage.EVENT_ORGANIZER_RATING_ALREADY_ADDED);});

        EventOrganizerRating eventOrganizerRating = modelMapper
            .map(rantingCreateDtoRequest, EventOrganizerRating.class);
        eventOrganizerRating.setOrganizer(event.getOrganizer());
        eventOrganizerRating.setUser(modelMapper.map(userVO, User.class));
        eventOrganizerRating.setEvent(event);

        eventOrganizerRatingRepo.save(eventOrganizerRating);
        return modelMapper.map(eventOrganizerRating, RantingDto.class);
    }

    @Override
    public Double find(Long eventId, UserVO userVO) {
        eventRepo.findById(eventId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));

        return eventOrganizerRatingRepo.findByEventIdAndUserId(eventId, userVO.getId()).stream()
            .mapToInt(EventOrganizerRating::getRating)
            .average().orElse(0);
    }
}
