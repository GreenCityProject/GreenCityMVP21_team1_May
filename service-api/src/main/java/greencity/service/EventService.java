package greencity.service;

import greencity.dto.event.EventCreateDtoResponse;
import greencity.dto.event.EventCreateDtoRequest;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {

    EventCreateDtoResponse create(EventCreateDtoRequest dto,
                                  MultipartFile[] images,
                                  String principal);
}
