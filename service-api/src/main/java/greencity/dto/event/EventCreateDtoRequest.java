package greencity.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventCreateDtoRequest {

    @NotNull(message = "title is null")
    private String title;

    @Valid
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();

    @NotNull(message = "description is null")
    private String description;

    private Boolean isOpen=true;
}