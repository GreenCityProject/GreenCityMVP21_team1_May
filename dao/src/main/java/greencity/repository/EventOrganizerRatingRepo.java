package greencity.repository;

import greencity.entity.EventOrganizerRating;
import greencity.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOrganizerRatingRepo extends JpaRepository<EventOrganizerRating, Long> {

    Optional<EventOrganizerRating> findByUser(User user);
}
