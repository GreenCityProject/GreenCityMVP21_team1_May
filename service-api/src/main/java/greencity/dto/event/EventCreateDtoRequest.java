package greencity.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 7, message = "Must add from 1 to 7 sets of date, time and location parameters")
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();

    @NotNull(message = "description is null")
    private String description;

    private Boolean isOpen=true;
}