package greencity.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserForEventDtoResponse {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Double organizerRating;

    @NotEmpty
    private String email;

}
