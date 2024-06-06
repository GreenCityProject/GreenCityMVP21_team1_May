package greencity.dto.event;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventDtoRequest {
    private String title;
    private List<EventDateLocationDtoRequest> dates = new ArrayList<>();
    private String description;
    private Boolean isOpen;
    private String titleImage;
    private List<AdditionalImageForEventDtoRequest> additionalImages = new ArrayList<>();
}