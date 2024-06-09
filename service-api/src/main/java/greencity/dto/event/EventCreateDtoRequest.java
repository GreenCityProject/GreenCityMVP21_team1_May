package greencity.dto.event;

import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventCreateDtoRequest {
    private String title;

    @Valid
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();
    private String description;
    private Boolean isOpen;
}