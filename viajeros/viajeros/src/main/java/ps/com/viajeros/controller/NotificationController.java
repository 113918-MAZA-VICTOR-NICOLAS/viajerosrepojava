package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.notification.NotificationResponseDto;
import ps.com.viajeros.entities.notification.NotificationEntity;
import ps.com.viajeros.entities.notification.NotificationStatus;
import ps.com.viajeros.repository.NotificationRepository;
import ps.com.viajeros.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public List<NotificationResponseDto> getNotificationsByUserId(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @PostMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        // Buscar la notificación por su ID
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        // Actualizar el estado de la notificación a 'READ'
        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);

        return ResponseEntity.ok("Notificación marcada como leída");
    }


}