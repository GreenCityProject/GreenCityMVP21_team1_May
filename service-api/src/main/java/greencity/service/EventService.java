package greencity.service;

import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.event.EventAttenderDto;
import greencity.dto.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventCreateDtoResponse create(EventCreateDtoRequest dto,
                                  MultipartFile[] images,
                                  String principal);

    List<EventCreateDtoResponse> getAll();

    EventCreateDtoResponse findEventById(Long id);

    EventCreateDtoResponse update(EventUpdateDtoRequest eventUpdate, MultipartFile[] images, UserVO user);

    /**
     * Method for adding attender to event.
     *
     * @param eventId id of event
     * @param user {@link UserVO} of authenticated user
     */
    void addAttender(Long eventId, UserVO user);

    /**
     * Method for deleting attender from event.
     *
     * @param eventId id of event
     * @param user {@link UserVO} of authenticated user
     */
    void deleteAttender(Long eventId, UserVO user);

    /**
     * Method for getting all event attenders by event id.
     *
     * @param eventId id of event
     * @return list of {@link UserVO}
     */
    List<EventAttenderDto> getAllEventAttenders(Long eventId, UserVO user);
}
