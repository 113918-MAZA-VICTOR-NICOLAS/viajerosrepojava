package ps.com.viajeros.entities.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mensaje de la notificación
    @Column(nullable = false)
    private String message;

    // Estado de la notificación (ej., leída o no leída)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;

    // Fecha y hora de la notificación
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Relación con el usuario que recibe la notificación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Tipo de notificación (ej., nuevo pasajero, mensaje sin leer)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}