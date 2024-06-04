package greencity.dto.event;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventDateLocationDtoRequest {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String onlineLink;

    private AddressDtoRequest address;
}
