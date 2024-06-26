package greencity.dto.eventorganizerranting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RantingDto {

    private Long id;
    private Integer rating;
    private Long organizerId;
    private Long userId;
    private Long eventId;
}
