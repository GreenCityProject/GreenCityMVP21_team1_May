package greencity.repository;

import greencity.entity.EventOrganizerRating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOrganizerRatingRepo extends JpaRepository<EventOrganizerRating, Long> {

    Optional<EventOrganizerRating> findByUserId(Long userId);

    List<EventOrganizerRating> findByEventIdAndUserId(Long eventId, Long userId);
}
