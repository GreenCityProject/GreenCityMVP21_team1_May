package greencity.dto.notifications;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class CreateNotificationDto implements Serializable {
    Long userId;
    Long senderId;
    @NotNull
    @Size(max = 255)
    String section;
    @NotNull
    @Size(max = 255)
    String title;
    @NotNull
    String message;
}