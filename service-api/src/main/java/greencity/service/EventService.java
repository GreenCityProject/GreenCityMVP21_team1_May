package greencity.service;

import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

public interface EventService {

    EventCreateDtoResponse create(EventCreateDtoRequest dto,
                                  MultipartFile[] images,
                                  String principal);

    List<EventCreateDtoResponse> getAll();

    EventCreateDtoResponse findEventById(Long id);

    EventCreateDtoResponse update(EventUpdateDtoRequest eventUpdate, MultipartFile[] images, UserVO user);

    void delete(Long eventId, String name);
}
