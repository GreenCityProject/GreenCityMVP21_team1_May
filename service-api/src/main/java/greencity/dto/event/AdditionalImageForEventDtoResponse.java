package greencity.dto.event;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AdditionalImageForEventDtoResponse {

    @NotEmpty
    private String data;
}
