package greencity.dto.notifications;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class NotificationDto implements Serializable {
    @NotNull
    @Min(1)
    Long id;
    @NotNull
    NotificationUserDto user;
    @NotNull
    NotificationUserDto senderUser;
    @NotNull
    @Size(max = 255)
    String section;
    @NotNull
    @Size(max = 255)
    String title;
    String message;
    @NotNull
    LocalDateTime createdAt;
    @NotNull
    Boolean isRead;
}