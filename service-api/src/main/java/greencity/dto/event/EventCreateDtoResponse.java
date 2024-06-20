package greencity.dto.event;

import greencity.dto.user.UserForEventDtoResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class EventCreateDtoResponse {
    private Long id;
    private LocalDateTime timestamp;
    private String title;
    private List<EventDateLocationDtoResponse> dates;
    private UserForEventDtoResponse organizer;
    private String description;
    private Boolean isOpen;
    private String titleImage;
    private List<AdditionalImageForEventDtoResponse> additionalImages;
}