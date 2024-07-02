package greencity.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDateLocationForEventsFilterDto {
    String country;
    String city;
    String onlineLink;
}
