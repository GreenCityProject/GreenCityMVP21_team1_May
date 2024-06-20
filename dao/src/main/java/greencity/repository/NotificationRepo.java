package greencity.repository;

import greencity.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :userId AND is_read = false ORDER BY created_at DESC LIMIT 3")
    List<Notification> getThreeLastUnreadNotificationsForUser(@Param("userId") Long id);

    List<Notification> findAllByUser_IdOrderByCreatedAtDesc(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :id AND is_read = false ORDER BY created_at DESC")
    List<Notification> findAllUnreadForUser(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :id AND is_read = false AND section = :section ORDER BY created_at DESC")
    List<Notification> findAllUnreadBySection(Long id, String section);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM notifications WHERE user_id = :id AND is_read = false")
    Long getAmountOfUnreadNotificationsByUserId(Long id);
}