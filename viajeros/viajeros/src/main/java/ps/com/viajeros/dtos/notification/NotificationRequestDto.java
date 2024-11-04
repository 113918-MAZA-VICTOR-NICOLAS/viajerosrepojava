package ps.com.viajeros.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ps.com.viajeros.entities.notification.NotificationStatus;
import ps.com.viajeros.entities.notification.NotificationType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {
    private String message;
    private Long userId; // ID del usuario que recibirá la notificación
    private NotificationType type;
}
