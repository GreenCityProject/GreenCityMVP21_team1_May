package greencity.service;

import greencity.entity.Notification;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.NotificationRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {
    @Mock
    private NotificationRepo notificationRepo;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("Test deleteById method with correct notificationId and userId")
    void deleteById_ValidData_NoExceptionThrown() {
        Notification notification = new Notification();
        when(notificationRepo.existsById(1L)).thenReturn(true);
        when(notificationRepo.findByIdAndUser_Id(1L, 1L)).thenReturn(notification);
        when(notificationRepo.findById(1L)).thenReturn(java.util.Optional.of(notification));

        notificationService.deleteById(1L, 1L);

        verify(notificationRepo, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteById method with incorrect notificationId")
    void deleteById_IncorrectNotificationId_ExceptionThrown() {
        when(notificationRepo.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> notificationService.deleteById(1L, 1L));
    }

    @Test
    @DisplayName("Test deleteById method when user has no permission to access delete notification")
    void deleteById_IncorrectUserId_ExceptionThrown() {
        Notification notification = new Notification();
        when(notificationRepo.existsById(1L)).thenReturn(true);
        when(notificationRepo.findByIdAndUser_Id(1L, 1L)).thenReturn(null);
        when(notificationRepo.findById(1L)).thenReturn(java.util.Optional.of(notification));

        assertThrows(UserHasNoPermissionToAccessException.class, () -> notificationService.deleteById(1L, 1L));
    }
}
