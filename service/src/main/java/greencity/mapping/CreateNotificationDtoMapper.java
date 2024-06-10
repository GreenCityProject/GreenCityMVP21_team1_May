package greencity.mapping;

import greencity.dto.notifications.CreateNotificationDto;
import greencity.entity.Notification;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class CreateNotificationDtoMapper extends AbstractConverter<CreateNotificationDto, Notification> {
    @Override
    public Notification convert(CreateNotificationDto createNotificationDto) {
        return Notification.builder()
            .title(createNotificationDto.getTitle())
            .message(createNotificationDto.getMessage())
            .section(createNotificationDto.getSection())
            .build();
    }
}
