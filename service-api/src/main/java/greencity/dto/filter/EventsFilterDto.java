package greencity.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsFilterDto {

    Boolean isUpcoming;
//    EventInitiativeType initiativeType; // Social, Economic, Environmental. // NOT IMPLEMENTED YET
    String country;
    String city;
    Boolean isOnline;
//    Double rating; // 1 star. 2 stars. 3. stars. Rating should be count according to average number of stars left by the users.// NOT IMPLEMENTED YET
//    EventStatus status; // Open, Closed, Joined, Saved, Created.// NOT IMPLEMENTED YET
    LocalDateTime startDateFrom;
    LocalDateTime endDateFrom;

}
