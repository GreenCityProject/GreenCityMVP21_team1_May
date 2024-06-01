package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.notifications.CreateNotification;
import greencity.dto.notifications.NotificationDto;
import greencity.entity.Notification;
import greencity.enums.NotificationSections;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.mapping.NotificationDtoMapper;
import greencity.repository.NotificationRepo;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;
    private final NotificationDtoMapper notificationMapper;

    @Override
    public List<NotificationDto> findAllForUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }

        return notificationRepo.findAllByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(notificationMapper::convert)
                .toList();
    }

    @Override
    public List<NotificationDto> getUnreadNotificationsForUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }

        return notificationRepo.findAllUnreadForUser(userId).stream()
                .map(notificationMapper::convert)
                .toList();
    }

    @Override
    public List<NotificationDto> getThreeLastUnreadNotificationsForUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }

        return notificationRepo.getThreeLastUnreadNotificationsForUser(userId).stream()
                .map(notificationMapper::convert)
                .toList();
    }

    @Override
    public List<NotificationDto> getUnreadBySection(Long userId, String section) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
        if (Arrays.stream(NotificationSections.values())
                .noneMatch(notificationSections -> notificationSections.name().equalsIgnoreCase(section))) {
            throw new BadRequestException("Section " + section + " does not exist");
        }

        return notificationRepo.findAllUnreadBySection(userId, section).stream()
                .map(notificationMapper::convert)
                .toList();
    }

    @Override
    public Long getAmountOfUnreadNotificationsByUserId(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
        return notificationRepo.getAmountOfUnreadNotificationsByUserId(userId);
    }

    @Override
    public NotificationDto save(CreateNotification createNotification) {
        return null;
    }

    @Override
    public NotificationDto findById(Long id) {
        return notificationRepo.findById(id)
                .map(notificationMapper::convert)
                .orElseThrow(() -> new NotFoundException("Notification with id " + id + " not found"));
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepo.findAllUnreadForUser(userId);
        if (notifications.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not have any unread notifications");
        }
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            notificationRepo.save(notification);
        });
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepo.findById(notificationId)
                .ifPresentOrElse(
                        notification -> {
                            notification.setIsRead(true);
                            notificationRepo.save(notification);
                        },
                        () -> {
                            throw new NotFoundException("Notification with id " + notificationId + " not found");
                        }
                );
    }
}
