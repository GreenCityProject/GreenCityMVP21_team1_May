package greencity.repository;

import greencity.entity.AdditionalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalImageRepo extends JpaRepository<AdditionalImage, Long> {
}
