package greencity.service;

import greencity.dto.eventorganizerranting.RantingCreateDtoRequest;
import greencity.dto.eventorganizerranting.RantingDto;
import greencity.dto.user.UserVO;

public interface EventOrganizerRatingService {

    /**
     * Creates a new rating for an event organizer.
     *
     * @param eventId                 the ID of the event
     * @param rantingCreateDtoRequest the DTO containing rating information
     * @param userVO                  the current user information
     * @return the created rating DTO
     */
    RantingDto create(Long eventId, RantingCreateDtoRequest rantingCreateDtoRequest, UserVO userVO);

    /**
     * Finds the average rating for an event organizer.
     *
     * @param eventId the ID of the event
     * @param userVO  the current user information
     * @return the average rating
     */
    Double find(Long eventId, UserVO userVO);
}
