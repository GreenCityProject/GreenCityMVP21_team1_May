package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventCreateDtoRequest;
import greencity.dto.event.EventUpdateDtoRequest;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface EventService {

    EventCreateDtoResponse create(EventCreateDtoRequest dto,
                                  MultipartFile[] images,
                                  String principal);

    List<EventCreateDtoResponse> getAll();

    EventCreateDtoResponse findEventById(Long id);

    EventCreateDtoResponse update(EventUpdateDtoRequest eventUpdate, MultipartFile[] images, UserVO user);

    PageableAdvancedDto<EventCreateDtoResponse> findEventByQuery(String query, Pageable pageable);

    void delete(Long eventId, String name);
}
