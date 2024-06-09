package greencity.service;

import greencity.dto.notifications.CreateNotificationDto;
import greencity.dto.notifications.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> findAllForUser(Long userId);

    List<NotificationDto> getUnreadNotificationsForUser(Long userId);

    List<NotificationDto> getThreeLastUnreadNotificationsForUser(Long userId);

    List<NotificationDto> getUnreadBySection(Long userId, String section);

    Long getAmountOfUnreadNotificationsByUserId(Long userId);

    NotificationDto save(CreateNotificationDto createNotification);

    NotificationDto findById(Long id);

    List<NotificationDto> markAllAsRead(Long userId);

    NotificationDto markAsRead(Long notificationId);

    void deleteById(Long notificationId, Long userId);
}
