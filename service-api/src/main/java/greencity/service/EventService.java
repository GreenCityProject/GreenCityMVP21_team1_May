package greencity.service;

import greencity.dto.event.EventDtoCreateResponse;
import greencity.dto.event.EventDtoRequest;

public interface EventService {

    EventDtoCreateResponse create(EventDtoRequest dto, String principal);
}
