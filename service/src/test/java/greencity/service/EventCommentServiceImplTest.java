package greencity.service;

import greencity.ModelUtils;
import greencity.dto.user.UserVO;
import greencity.entity.EventComment;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EventCommentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EventCommentServiceImplTest {

    @Mock
    private EventCommentRepo eventCommentRepo;
    @InjectMocks
    private EventCommentServiceImpl eventCommentService;

    private EventComment eventComment;
    private final UserVO author = ModelUtils.getUserVO();
    private final UserVO anotherUser = ModelUtils.getUserVO().setId(2L);

    @BeforeEach
    public void setup() {
        eventComment = new EventComment();
        eventComment.setId(1L);
        eventComment.setAuthor(ModelUtils.getUser());
    }


    @Test
    @DisplayName("Test delete method success")
    void delete_shouldDeleteComment_whenUserIsAuthor() {

        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(eventComment));
        eventCommentService.delete(1L, author);

        verify(eventCommentRepo).deleteById(1L);
    }

    @Test
    @DisplayName("Test delete method throwing NotFoundException")
    void delete_shouldThrowNotFoundException_whenCommentDoesNotExist() {

        when(eventCommentRepo.findById(1L)).thenReturn(Optional.empty());


        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventCommentService.delete(1L, author));

        assertEquals("Event comment not found with id: 1", thrown.getMessage());
        verify(eventCommentRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test delete method throwing UserHasNoPermissionToAccessException")
    void delete_shouldThrowBadRequestException_whenUserIsNotAuthor() {
        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(eventComment));

        UserHasNoPermissionToAccessException thrown = assertThrows(UserHasNoPermissionToAccessException.class, () -> eventCommentService.delete(1L, anotherUser));

        assertEquals("You are not allowed to delete other people's comments", thrown.getMessage());
        verify(eventCommentRepo, never()).deleteById(anyLong());
    }

}
