package greencity.repository;

import greencity.entity.EventDateLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDateLocationRepo extends JpaRepository<EventDateLocation, Long> {
}