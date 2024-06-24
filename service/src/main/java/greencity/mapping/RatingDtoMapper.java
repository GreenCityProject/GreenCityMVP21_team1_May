package greencity.mapping;

import greencity.dto.eventorganizerranting.RantingDto;
import greencity.entity.EventOrganizerRating;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class RatingDtoMapper extends AbstractConverter<EventOrganizerRating, RantingDto> {
    @Override
    protected RantingDto convert(EventOrganizerRating eventOrganizerRating) {
        return RantingDto.builder()
            .id(eventOrganizerRating.getId())
            .rating(eventOrganizerRating.getRating())
            .organizerId(eventOrganizerRating.getOrganizer().getId())
            .userId(eventOrganizerRating.getUser().getId())
            .eventId(eventOrganizerRating.getEvent().getId())
            .build();
    }
}
