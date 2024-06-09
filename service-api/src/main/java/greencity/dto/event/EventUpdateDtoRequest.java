package greencity.dto.event;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventUpdateDtoRequest {
    private Long id;
    private String title;
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();
    private String description;
}
