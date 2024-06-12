package greencity.service;

import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.EventCommentDtoResponse;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventComment;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.EventCommentRepo;
import greencity.repository.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventCommentServiceImpl implements EventCommentService{
    private final EventRepo eventRepo;
    private final EventCommentRepo eventCommentRepo;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public EventCommentDtoResponse save(Long eventId, AddEventCommentDtoRequest request, UserVO user) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        EventComment eventComment = new EventComment();
        eventComment.setText(request.getText());
        eventComment.setAuthor(modelMapper.map(user, User.class));
        eventComment.setEvent(event);
        eventComment.setCreatedDate(LocalDateTime.now());

        Long repliedTo = request.getRepliedTo();
        if (repliedTo != null && repliedTo != 0){
            Optional<EventComment> eventCommentOptional = eventCommentRepo.findById(repliedTo);
            if (eventCommentOptional.isPresent()){
                eventComment.setRepliedTo(eventCommentOptional.get());
            }
        }
        // todo: check request and response
        return modelMapper.map(eventCommentRepo.save(eventComment), EventCommentDtoResponse.class);
    }
}
