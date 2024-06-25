package greencity.repository;

import greencity.entity.NicknamesArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NicknamesArchiveRepo extends JpaRepository<NicknamesArchive, Long> {
}
