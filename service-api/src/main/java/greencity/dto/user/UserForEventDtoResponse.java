package greencity.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserForEventDtoResponse {
    private Long id;
    private String name;
    private Double organizerRating;
    private String email;
}
