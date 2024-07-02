package greencity.dto.filter;

import greencity.enums.EventInitiativeType;
import greencity.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsFilterDto {

    Boolean isUpcoming; // Upcoming, passed
    EventInitiativeType initiativeType; // Social, Economic, Environmental.
    EventDateLocationForEventsFilterDto location;// Online, Offline (Country, City) (only for authorized users)
    Double rating;// 1 star. 2 stars. 3. stars. Rating should be count according to average number of stars left by the users.
    EventStatus status;// Open, Closed, Joined, Saved, Created.
    LocalDateTime startDate;// User can choose the date of beginning and end of the event.
    LocalDateTime endDate;// User can choose the date of beginning and end of the event.

}
