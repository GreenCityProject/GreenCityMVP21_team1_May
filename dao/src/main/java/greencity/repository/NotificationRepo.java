package greencity.repository;

import greencity.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :userId AND is_read = false ORDER BY created_at DESC LIMIT 3")
    List<Notification> getThreeLastUnreadNotificationsForUser(@Param("userId") Long id);

    List<Notification> findAllByUser_IdOrderByCreatedAtDesc(Long id);
    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :userId AND is_read = false ORDER BY created_at DESC")
    List<Notification> findAllUnreadForUser(@Param("userId") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM notifications WHERE user_id = :userId AND is_read = false AND section = :section ORDER BY created_at DESC")
    List<Notification> findAllUnreadBySection(@Param("userId") Long id, @Param("section") String section);

    Notification findByIdAndUser_Id(Long id, Long userId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = false")
    Long getAmountOfUnreadNotificationsByUserId(@Param("userId") Long id);


}