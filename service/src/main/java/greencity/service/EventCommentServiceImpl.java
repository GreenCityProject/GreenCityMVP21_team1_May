package greencity.service;

import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.EventCommentDtoResponse;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventComment;
import greencity.entity.User;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.EventCommentRepo;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        if (repliedTo != null && repliedTo != 0) {
            Optional<EventComment> repliedToComment = eventCommentRepo.findById(repliedTo);
            if (repliedToComment.isPresent()) {
                Optional<Event> eventOfRepliedComment = eventRepo.findById(repliedToComment.get().getEvent().getId());
                if (eventOfRepliedComment.isPresent()) {
                    if(eventOfRepliedComment.get().getId().equals(event.getId())){
                        eventComment.setRepliedTo(repliedToComment.get());
                    } else throw new BadRequestException("You trying to reply to a comment for another event");
                }
            } else throw new NotFoundException("You trying to reply to a not found comment: " + repliedTo);
        }


        return modelMapper.map(eventCommentRepo.save(eventComment), EventCommentDtoResponse.class);
    }

    @Override
    public List<EventCommentDtoResponse> getAllByEventId(Long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        if (event.isPresent()){
            List<EventComment> eventComments = this.eventCommentRepo.findAllByEventId(eventId);
            return eventComments.stream()
                    .map(e -> modelMapper.map(e, EventCommentDtoResponse.class))
                    .toList();
        } else throw new NotFoundException("Event not found");
    }

    @Override
    public EventCommentDtoResponse findCommentById(Long commentId) {
        EventComment eventComment = this.eventCommentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        return modelMapper.map(eventComment, EventCommentDtoResponse.class);
    }


}
