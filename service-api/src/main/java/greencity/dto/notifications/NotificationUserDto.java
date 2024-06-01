package greencity.dto.notifications;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class NotificationUserDto implements Serializable {
    @NotNull
    @Min(1)
    Long id;
    @NotNull
    String name;
}