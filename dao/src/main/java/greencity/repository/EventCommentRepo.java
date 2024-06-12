package greencity.repository;


import greencity.entity.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCommentRepo extends JpaRepository<EventComment, Long> {
}
