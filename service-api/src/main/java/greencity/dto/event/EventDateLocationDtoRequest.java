package greencity.dto.event;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventDateLocationDtoRequest {

    @NotNull(message = "Start time must not be NULL")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be NULL")
    private LocalDateTime endTime;

    private String onlineLink;

    @Valid
    @NotNull(message = "Address must not be NULL")
    private AddressDtoRequest address;

    @PostConstruct
    public void throwIfStartTimeIsBeforeEnd() {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
}
