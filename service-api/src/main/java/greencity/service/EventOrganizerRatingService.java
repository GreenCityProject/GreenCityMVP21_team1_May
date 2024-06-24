package greencity.service;

import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingDto;
import greencity.dto.user.UserVO;

public interface EventOrganizerRatingService {

    RantingDto create(Long eventId, RantingCreateDtoRequest rantingCreateDtoRequest, UserVO userVO);

    Double find(Long eventId, UserVO userVO);
}
