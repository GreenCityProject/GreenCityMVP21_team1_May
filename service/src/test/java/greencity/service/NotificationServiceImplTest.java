package greencity.service;


import greencity.constant.ErrorMessage;
import greencity.dto.notifications.NotificationDto;
import greencity.entity.Notification;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.NotificationRepo;
import greencity.repository.UserRepo;
import greencity.mapping.NotificationDtoMapper;
import greencity.mapping.CreateNotificationDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static greencity.ModelUtils.*;
import static greencity.ModelUtils.getCreateNotificationDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {
    @Mock
    private NotificationRepo notificationRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private NotificationDtoMapper notificationMapper;
    @Mock
    private CreateNotificationDtoMapper createNotificationDtoMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("findAllForUser throws NotFoundException if user not found by id")
    void findAllForUser_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;

        when(userRepo.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.findAllForUser(userId));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).findAllByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("findAllForUser returns notifications")
    void findAllForUser_ReturnsNotifications_ValidUser() {
        Long userId = 1L;
        NotificationDto notificationDto = getNotificationDto();
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.findAllByUser_IdOrderByCreatedAtDesc(userId)).thenReturn(List.of(getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.findAllForUser(userId);

        assertEquals(1, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).findAllByUser_IdOrderByCreatedAtDesc(userId);
        verify(notificationMapper, times(1)).convert((Notification) any());
    }

    @Test
    @DisplayName("getUnreadNotificationsForUser throws NotFoundException if user not found by id")
    void getUnreadNotificationsForUser_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;

        when(userRepo.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.getUnreadNotificationsForUser(userId));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).findAllUnreadForUser(userId);
    }

    @Test
    @DisplayName("getUnreadNotificationsForUser returns notifications")
    void getUnreadNotificationsForUser_ReturnsNotifications_ValidUser() {
        Long userId = 1L;
        NotificationDto notificationDto = getNotificationDto();
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.findAllUnreadForUser(userId)).thenReturn(List.of(getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.getUnreadNotificationsForUser(userId);

        assertEquals(1, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        assertFalse(notifications.getFirst().getIsRead());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).findAllUnreadForUser(userId);
        verify(notificationMapper, times(1)).convert((Notification) any());
    }

    @Test
    @DisplayName("getThreeLastUnreadNotificationsForUser throws NotFoundException if user not found by id")
    void getThreeLastUnreadNotificationsForUser_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;

        when(userRepo.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.getThreeLastUnreadNotificationsForUser(userId));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).getThreeLastUnreadNotificationsForUser(userId);
    }

    @Test
    @DisplayName("getThreeLastUnreadNotificationsForUser returns notifications with 3 elements when there are more than 3")
    void getThreeLastUnreadNotificationsForUser_ReturnsThreeNotifications_WithMoreThanThreeUnread() {
        Long userId = 1L;
        NotificationDto notificationDto = getNotificationDto();
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.getThreeLastUnreadNotificationsForUser(userId)).thenReturn(List.of(getNotification(), getNotification(), getNotification(), getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.getThreeLastUnreadNotificationsForUser(userId);

        assertEquals(3, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        assertFalse(notifications.getFirst().getIsRead());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).getThreeLastUnreadNotificationsForUser(userId);
        verify(notificationMapper, times(3)).convert((Notification) any());
    }

    @Test
    @DisplayName("getThreeLastUnreadNotificationsForUser returns notifications with 2 elements when there are less than 3")
    void getThreeLastUnreadNotificationsForUser_ReturnsTwoNotifications_WithLessThanThreeUnread() {
        Long userId = 1L;
        NotificationDto notificationDto = getNotificationDto();
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.getThreeLastUnreadNotificationsForUser(userId)).thenReturn(List.of(getNotification(), getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.getThreeLastUnreadNotificationsForUser(userId);

        assertEquals(2, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        assertFalse(notifications.getFirst().getIsRead());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).getThreeLastUnreadNotificationsForUser(userId);
        verify(notificationMapper, times(2)).convert((Notification) any());
    }

    @Test
    @DisplayName("getUnreadBySection throws NotFoundException if user not found by id")
    void getUnreadBySection_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;
        String section = "section";

        when(userRepo.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.getUnreadBySection(userId, section));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).findAllUnreadBySection(userId, section);
    }

    @Test
    @DisplayName("getUnreadBySection throws BadRequestException if section does not exist")
    void getUnreadBySection_ThrowsBadRequestException_SectionNotFound() {
        Long userId = 1L;
        String section = "section";

        when(userRepo.existsById(userId)).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> notificationService.getUnreadBySection(userId, section));

        assertEquals("Section " + section + " does not exist", exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).findAllUnreadBySection(userId, section);
    }

    @Test
    @DisplayName("getUnreadBySection returns notifications")
    void getUnreadBySection_ReturnsNotifications_ValidData() {
        Long userId = 1L;
        String section = "GrEeNcItY";
        NotificationDto notificationDto = getNotificationDto();
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.findAllUnreadBySection(userId, section)).thenReturn(List.of(getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.getUnreadBySection(userId, section);

        assertEquals(1, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        assertFalse(notifications.getFirst().getIsRead());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).findAllUnreadBySection(userId, section);
        verify(notificationMapper, times(1)).convert((Notification) any());
    }

    @Test
    @DisplayName("getAmountOfUnreadNotificationsByUserId throws NotFoundException if user not found by id")
    void getAmountOfUnreadNotificationsByUserId_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;

        when(userRepo.existsById(userId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.getAmountOfUnreadNotificationsByUserId(userId));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, never()).getAmountOfUnreadNotificationsByUserId(userId);
    }

    @Test
    @DisplayName("getAmountOfUnreadNotificationsByUserId returns amount of unread notifications")
    void getAmountOfUnreadNotificationsByUserId_ReturnsAmount_ValidUser() {
        Long userId = 1L;
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.getAmountOfUnreadNotificationsByUserId(userId)).thenReturn(Long.valueOf(List.of(getNotification()).size()));

        Long result = notificationService.getAmountOfUnreadNotificationsByUserId(userId);

        assertEquals(1, result);
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).getAmountOfUnreadNotificationsByUserId(userId);
    }

    @Test
    @DisplayName("findById throws NotFoundException if notification not found by id")
    void findById_ThrowsNotFoundException_NotificationNotFound() {
        Long id = 100L;

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.findById(id));

        assertEquals("Notification with id " + id + " not found", exception.getMessage());
        verify(notificationRepo, times(1)).findById(id);
    }

    @Test
    @DisplayName("findById returns notification")
    void findById_ReturnsNotification_ValidId() {
        Long id = 1L;
        NotificationDto notificationDto = getNotificationDto();
        when(notificationRepo.findById(id)).thenReturn(Optional.of(getNotification()));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        NotificationDto result = notificationService.findById(id);

        assertEquals(notificationDto, result);
        verify(notificationRepo, times(1)).findById(id);
        verify(notificationMapper, times(1)).convert((Notification) any());
    }

    @Test
    @DisplayName("save throws NotFoundException if user not found by id")
    void save_ThrowsNotFoundException_UserNotFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.save(getCreateNotificationDto()));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + 1, exception.getMessage());
        verify(userRepo, times(1)).findById(anyLong());
        verify(notificationRepo, never()).save(any());
    }

    @Test
    @DisplayName("save throws NotFoundException if sender user not found by id")
    void save_ThrowsNotFoundException_SenderUserNotFound() {
        when(createNotificationDtoMapper.convert(getCreateNotificationDto())).thenReturn(getNotification());
        when(userRepo.findById(getCreateNotificationDto().getUserId())).thenReturn(Optional.of(getUser()));
        when(userRepo.findById(getCreateNotificationDto().getSenderId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.save(getCreateNotificationDto()));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + getCreateNotificationDto().getSenderId(), exception.getMessage());

        verify(createNotificationDtoMapper, times(1)).convert(getCreateNotificationDto());
        verify(userRepo, times(1)).findById(getCreateNotificationDto().getUserId());
        verify(userRepo, times(1)).findById(getCreateNotificationDto().getSenderId());
        verify(notificationRepo, never()).save(any());
    }

    @Test
    @DisplayName("save returns notification")
    void save_ReturnsNotification_Saved() {
        Notification notification = getNotification();
        NotificationDto expectedNotificationDto = getNotificationDto();

        when(createNotificationDtoMapper.convert(getCreateNotificationDto())).thenReturn(notification);
        when(userRepo.findById(getCreateNotificationDto().getUserId())).thenReturn(Optional.of(getUser()));
        when(userRepo.findById(getCreateNotificationDto().getSenderId())).thenReturn(Optional.of(getUser()));
        when(notificationRepo.save(notification)).thenReturn(notification);
        when(notificationMapper.convert(notification)).thenReturn(expectedNotificationDto);

        NotificationDto actualNotificationDto = notificationService.save(getCreateNotificationDto());

        assertEquals(expectedNotificationDto, actualNotificationDto);

        verify(createNotificationDtoMapper, times(1)).convert(getCreateNotificationDto());
        verify(userRepo, times(1)).findById(getCreateNotificationDto().getUserId());
        verify(userRepo, times(1)).findById(getCreateNotificationDto().getSenderId());
        verify(notificationRepo, times(1)).save(notification);
        verify(notificationMapper, times(1)).convert(notification);
    }

    @Test
    @DisplayName("markAllAsRead throws NotFoundException if user not found by id")
    void markAllAsRead_ThrowsNotFoundException_UserNotFound() {
        Long userId = 1L;
        when(userRepo.existsById(userId)).thenReturn(false);
        when(notificationRepo.findAllUnreadForUser(userId)).thenReturn(new ArrayList<>());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.markAllAsRead(userId));

        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_ID + userId, exception.getMessage());

        verify(userRepo, times(1)).existsById(userId);
    }

    @Test
    @DisplayName("markAllAsRead throws NotFoundException if user does not have any unread notifications")
    void markAllAsRead_ThrowsNotFoundException_NoUnreadNotifications() {
        Long userId = 1L;
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.findAllUnreadForUser(userId)).thenReturn(new ArrayList<>());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.markAllAsRead(userId));

        assertEquals("User with id " + userId + " does not have any unread notifications", exception.getMessage());

        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).findAllUnreadForUser(userId);
        verify(notificationRepo, never()).save(any(Notification.class));
        verify(notificationMapper, never()).convert(any(Notification.class));
    }

    @Test
    @DisplayName("markAllAsRead returns notifications")
    void markAllAsRead_ReturnsNotifications_ValidUser() {
        Long userId = 1L;
        NotificationDto notificationDto = getNotificationDto();
        notificationDto.setIsRead(true);
        Notification notification = getNotification();
        notification.setIsRead(false);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(notificationRepo.findAllUnreadForUser(userId)).thenReturn(List.of(notification));
        when(notificationMapper.convert((Notification) any())).thenReturn(notificationDto);

        List<NotificationDto> notifications = notificationService.markAllAsRead(userId);

        assertEquals(1, notifications.size());
        assertEquals(notificationDto, notifications.getFirst());
        assertTrue(notifications.getFirst().getIsRead());
        verify(userRepo, times(1)).existsById(userId);
        verify(notificationRepo, times(1)).findAllUnreadForUser(userId);
        verify(notificationMapper, times(1)).convert((Notification) any());
    }

    @Test
    @DisplayName("markAsRead throws NotFoundException if notification not found by id")
    void markAsRead_ThrowsNotFoundException_NotificationNotFound() {
        Long notificationId = 100L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> notificationService.markAsRead(notificationId));

        assertEquals("Notification with id " + notificationId + " not found", exception.getMessage());

        verify(notificationRepo, times(1)).findById(notificationId);
        verify(notificationRepo, never()).save(any(Notification.class));
        verify(notificationMapper, never()).convert(any(Notification.class));
    }

    @Test
    @DisplayName("markAsRead returns notification")
    void markAsRead_ReturnsNotification_Success() {
        Long notificationId = 1L;
        Notification notification = getNotification();
        NotificationDto expectedNotificationDto = getNotificationDto();

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepo.save(notification)).thenReturn(notification);
        when(notificationMapper.convert(notification)).thenReturn(expectedNotificationDto);

        NotificationDto actualNotificationDto = notificationService.markAsRead(notificationId);

        assertEquals(expectedNotificationDto, actualNotificationDto);

        verify(notificationRepo, times(1)).findById(notificationId);
        verify(notificationRepo, times(1)).save(notification);
        verify(notificationMapper, times(1)).convert(notification);
    }

}