package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.notification.NotificationRequestDto;
import ps.com.viajeros.dtos.notification.NotificationResponseDto;

import java.util.List;

@Service
public interface NotificationService {

    void createNotification(NotificationRequestDto dto);

    List<NotificationResponseDto> getNotificationsByUserId(Long userId);
}
