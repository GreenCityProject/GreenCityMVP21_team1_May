package greencity.dto.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AdditionalImageForEventDtoResponse {
    private Long id;
    private String data;
}
