package greencity.service;

import greencity.client.RestClient;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.User;
import greencity.repository.EventRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepo eventRepo;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    public void deleteEventNotFound_ShouldThrowNotFoundException() {
        Long eventId = 1L;
        String email = "user@domain.com";

        when(restClient.findByEmail(email)).thenReturn(new UserVO());
        when(eventRepo.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            eventService.delete(eventId, email);
        });
    }

    @Test
    public void deleteEventUserNotPermitted_ShouldThrowUserHasNoPermissionException() {
        Long eventId = 1L;
        String email = "user@domain.com";
        UserVO user = new UserVO();
        Event event = new Event();
        User organizer = new User();
        organizer.setId(2L);
        event.setOrganizer(organizer);
        user.setId(1L);

        when(restClient.findByEmail(email)).thenReturn(user);
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(UserHasNoPermissionToAccessException.class, () -> {
            eventService.delete(eventId, email);
        });
    }

    @Test
    public void deleteEventSuccess_NoException() {
        Long eventId = 1L;
        String email = "user@domain.com";
        UserVO user = new UserVO();
        Event event = new Event();
        User organizer = new User();
        organizer.setId(1L);
        event.setOrganizer(organizer);
        user.setId(1L);

        when(restClient.findByEmail(email)).thenReturn(user);
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));

        assertDoesNotThrow(() -> {
            eventService.delete(eventId, email);
        });
    }
}