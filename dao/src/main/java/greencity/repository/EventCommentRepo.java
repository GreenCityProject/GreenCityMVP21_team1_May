package greencity.repository;


import greencity.entity.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventCommentRepo extends JpaRepository<EventComment, Long> {
    List<EventComment> findAllByEventIdOrderByCreatedDateDesc(Long eventId);

    Integer countByEventId(Long eventId);
}
