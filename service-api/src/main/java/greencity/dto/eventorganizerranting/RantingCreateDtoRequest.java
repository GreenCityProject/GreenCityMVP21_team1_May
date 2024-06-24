package greencity.dto.eventorganizerranting;

import greencity.annotations.OrganizerRatingValidation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RantingCreateDtoRequest {

    @OrganizerRatingValidation
    Integer rating;
}
