package greencity.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserForEventDtoResponse {//todo: why all null except id
    private Long id;
    private String name;
    private Double organizerRating;
    private String email;
}
