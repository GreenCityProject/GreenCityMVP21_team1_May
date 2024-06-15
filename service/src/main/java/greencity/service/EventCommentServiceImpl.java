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
import jakarta.transaction.Transactional;
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

    private static final String EVENT_NOT_FOUND = "Event not found";

    @Override
    @Transactional
    public EventCommentDtoResponse save(Long eventId, AddEventCommentDtoRequest request, UserVO user) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        EventComment eventComment = new EventComment();
        eventComment.setText(request.getText());
        eventComment.setAuthor(modelMapper.map(user, User.class));
        eventComment.setEvent(event);
        eventComment.setCreatedDate(LocalDateTime.now());

        Long repliedTo = request.getRepliedTo();
        if (repliedTo != null && repliedTo != 0) {
            EventComment repliedToComment = eventCommentRepo.findById(repliedTo).orElseThrow(() -> new NotFoundException("You trying to reply to a not found comment: " + repliedTo));
            if(repliedToComment.getRepliedTo() != null){
                throw new BadRequestException("You can't reply on reply");
            }
            Optional<Event> eventOfRepliedComment = eventRepo.findById(repliedToComment.getEvent().getId());
            if (eventOfRepliedComment.isPresent()) {
                if (eventOfRepliedComment.get().getId().equals(event.getId())) {
                    eventComment.setRepliedTo(repliedToComment);
                } else throw new BadRequestException("You trying to reply to a comment for another event");
            }
        }

        return modelMapper.map(eventCommentRepo.save(eventComment), EventCommentDtoResponse.class);
    }

    @Override
    @Transactional
    public List<EventCommentDtoResponse> getAllByEventId(Long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        if (event.isPresent()){
            List<EventComment> eventComments = this.eventCommentRepo.findAllByEventIdOrderByCreatedDateDesc(eventId);
            return eventComments.stream()
                    .map(e -> modelMapper.map(e, EventCommentDtoResponse.class))
                    .toList();
        } else throw new NotFoundException(EVENT_NOT_FOUND);
    }

    @Override
    @Transactional
    public EventCommentDtoResponse findCommentById(Long commentId) {
        EventComment eventComment = this.eventCommentRepo.findById(commentId).orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));
        return modelMapper.map(eventComment, EventCommentDtoResponse.class);
    }

    @Override
    @Transactional
    public Integer getCommentsCountByEventId(Long eventId) {
        Optional<Event> event = eventRepo.findById(eventId);
        if (event.isPresent()){
            return eventCommentRepo.countByEventId(eventId);
        } else throw new NotFoundException(EVENT_NOT_FOUND);
    }


}
