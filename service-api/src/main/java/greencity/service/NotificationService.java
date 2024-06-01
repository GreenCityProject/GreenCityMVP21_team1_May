package greencity.service;

import greencity.dto.notifications.CreateNotification;
import greencity.dto.notifications.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> findAllForUser(Long userId);

    List<NotificationDto> getUnreadNotificationsForUser(Long userId);

    List<NotificationDto> getThreeLastUnreadNotificationsForUser(Long userId);

    List<NotificationDto> getUnreadBySection(Long userId, String section);

    Long getAmountOfUnreadNotificationsByUserId(Long userId);

    NotificationDto save(CreateNotification createNotification);

    NotificationDto findById(Long id);

    void markAllAsRead(Long userId);

    void markAsRead(Long notificationId);
}
