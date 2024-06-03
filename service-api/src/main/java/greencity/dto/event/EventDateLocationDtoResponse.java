package greencity.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventDateLocationDtoResponse {

    private Long id;

    private Long eventId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String onlineLink;

    private AddressDtoResponse address;
}

