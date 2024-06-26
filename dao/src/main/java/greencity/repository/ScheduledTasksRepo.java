package greencity.repository;

import greencity.entity.ScheduledTask;
import greencity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ScheduledTasksRepo extends JpaRepository<ScheduledTask, Long> {
    Optional<ScheduledTask> findByTask(String task);

    /**
     * Updates done status for a given task.
     *
     * @param taskId               - {@link ScheduledTask}'s id
     * @param isDone - new {@link ScheduledTask}'s done parameter
     * @author Dmytro Fedotov
     */
    @Modifying
    @Transactional
    @Query("UPDATE ScheduledTask s SET s.done = :isDone WHERE s.id = :taskId")
    void updateScheduledTaskDone(Long taskId, boolean isDone);
}
