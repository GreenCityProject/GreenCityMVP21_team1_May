package greencity.mapping;

import greencity.dto.event.EventAttenderDto;
import greencity.entity.User;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EventAttenderMapper extends AbstractConverter<User, EventAttenderDto> {
    @Override
    protected EventAttenderDto convert(User user) {
        return EventAttenderDto.builder()
                .id(user.getId())
                .name(user.getFirstName())
                .build();
    }
}
