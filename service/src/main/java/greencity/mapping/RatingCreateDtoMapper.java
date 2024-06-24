package greencity.mapping;

import greencity.dto.eventorganizerranting.RantingCreateDtoResponse;
import greencity.entity.EventOrganizerRating;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class RatingCreateDtoMapper extends AbstractConverter<EventOrganizerRating, RantingCreateDtoResponse> {
    @Override
    protected RantingCreateDtoResponse convert(EventOrganizerRating eventOrganizerRating) {
        return RantingCreateDtoResponse.builder()
            .id(eventOrganizerRating.getId())
            .rating(eventOrganizerRating.getRating())
            .organizerId(eventOrganizerRating.getOrganizer().getId())
            .userId(eventOrganizerRating.getUser().getId())
            .eventId(eventOrganizerRating.getEvent().getId())
            .build();
    }
}
