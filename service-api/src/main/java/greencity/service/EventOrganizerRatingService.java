package greencity.service;

import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingCreateDtoResponse;
import greencity.dto.user.UserVO;

public interface EventOrganizerRatingService {

    RantingCreateDtoResponse create(Long eventId, RantingCreateDtoRequest rantingCreateDtoRequest, UserVO userVO);
}
