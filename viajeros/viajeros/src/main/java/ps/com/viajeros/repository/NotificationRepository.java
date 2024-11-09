package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.notification.NotificationEntity;
import ps.com.viajeros.entities.notification.NotificationStatus;
import ps.com.viajeros.entities.notification.NotificationType;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUser(UserEntity user);
    // Encuentra una notificación por usuario y tipo de notificación
    Optional<NotificationEntity> findByUserAndType(UserEntity user, NotificationType type);
    List<NotificationEntity> findByUserAndStatus(UserEntity user, NotificationStatus status);



}
