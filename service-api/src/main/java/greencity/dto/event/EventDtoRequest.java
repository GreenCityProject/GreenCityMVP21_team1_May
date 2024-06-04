package greencity.dto.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventDtoRequest {

    @NotEmpty
    private String title;

    @Size(min = 1)
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();

    @NotEmpty
    private String description;

    @NotNull
    private Boolean isOpen;

    @NotEmpty
    private String titleImage;

    private List<AdditionalImageForEventDtoRequest> additionalImages = new ArrayList<>();
}