package greencity.service;

import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.EventCommentDtoResponse;
import greencity.dto.user.UserVO;

import java.util.List;

public interface EventCommentService {
    EventCommentDtoResponse save(Long eventId, AddEventCommentDtoRequest request, UserVO user);

    List<EventCommentDtoResponse> getAllByEventId(Long eventId);

    EventCommentDtoResponse findCommentById(Long commentId);

    Integer getCommentsCountByEventId(Long eventId);
}
