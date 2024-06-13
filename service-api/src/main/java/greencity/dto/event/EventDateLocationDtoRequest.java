package greencity.dto.event;

import greencity.annotations.ValidStartBeforeEndDates;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@ValidStartBeforeEndDates(message = "Start time and date must be before end date")
public class EventDateLocationDtoRequest {

    @NotNull(message = "Start time must not be NULL")
    @Future(message = "Start time and date must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be NULL")
    @Future(message = "Start time and date must be in the future")
    private LocalDateTime endTime;

    @URL
    private String onlineLink;

    @Valid
    @NotNull(message = "Address must not be NULL")
    private AddressDtoRequest address;

}
