package greencity.dto.event;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class EventShortInfoDto {
    private Long id;
    private String title;
    private String description;
}