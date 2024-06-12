package greencity.service;

import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.EventCommentDtoResponse;
import greencity.dto.user.UserVO;

public interface EventCommentService {
    EventCommentDtoResponse save(Long eventId, AddEventCommentDtoRequest request, UserVO user);
}
