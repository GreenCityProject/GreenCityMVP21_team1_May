package greencity.mapping;

import greencity.dto.notifications.NotificationDto;
import greencity.dto.notifications.NotificationUserDto;
import greencity.entity.Notification;
import greencity.entity.User;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper extends AbstractConverter<Notification, NotificationDto> {
    @Override
    public NotificationDto convert(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .user(convertUser(notification.getUser()))
                .senderUser(convertUser(notification.getSenderUser()))
                .section(notification.getSection())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .build();
    }

    private NotificationUserDto convertUser(User user) {
        return NotificationUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
