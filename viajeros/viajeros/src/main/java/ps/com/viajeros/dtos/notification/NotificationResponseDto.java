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
public class NotificationResponseDto {
    private Long id;
    private String message;
    private NotificationStatus status;
    private LocalDateTime timestamp;
    private NotificationType type;

}
