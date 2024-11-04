package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.notification.NotificationRequestDto;
import ps.com.viajeros.dtos.notification.NotificationResponseDto;
import ps.com.viajeros.entities.notification.NotificationEntity;
import ps.com.viajeros.entities.notification.NotificationStatus;
import ps.com.viajeros.entities.notification.NotificationType;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.repository.ChatRepository;
import ps.com.viajeros.repository.NotificationRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.NotificationService;
import ps.com.viajeros.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    ChatRepository chatRepository;
    @Override
    public void createNotification(NotificationRequestDto dto) {

        UserEntity userEntity = userRepository.getReferenceById(dto.getUserId());


        NotificationEntity notification = new NotificationEntity();
        notification.setMessage(dto.getMessage());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setTimestamp(LocalDateTime.now());
        notification.setUser(userEntity); // Suponiendo que tienes el usuario referenciado por ID
        notification.setType(dto.getType());

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
        // Lista de notificaciones
        List<NotificationResponseDto> notifications = new ArrayList<>();

        // Obtener el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si hay mensajes sin leer
        boolean hasUnreadMessages = chatRepository.findByChoferOrPasajero(user, user)
                .stream()
                .flatMap(chat -> chat.getMensajes().stream())
                .anyMatch(message -> !message.isLeido() && message.getUsuario().getIdUser().equals(userId));

        // Si hay mensajes sin leer, y no hay una notificación UNREAD_MESSAGE previa
        if (hasUnreadMessages && notificationRepository.findByUserAndType(user, NotificationType.UNREAD_MESSAGE).isEmpty()) {
            // Crear y guardar la notificación en la base de datos
            NotificationEntity unreadMessageNotification = new NotificationEntity();
            unreadMessageNotification.setUser(user);
            unreadMessageNotification.setMessage("Tienes mensajes sin leer");
            unreadMessageNotification.setStatus(NotificationStatus.UNREAD);
            unreadMessageNotification.setTimestamp(LocalDateTime.now());
            unreadMessageNotification.setType(NotificationType.UNREAD_MESSAGE);
            notificationRepository.save(unreadMessageNotification);
        }

        // Agregar todas las notificaciones del usuario desde NotificationRepository
        notifications.addAll(notificationRepository.findByUser(user)
                .stream()
                .map(notification -> {
                    NotificationResponseDto dto = new NotificationResponseDto();
                    dto.setId(notification.getId());
                    dto.setMessage(notification.getMessage());
                    dto.setStatus(notification.getStatus());
                    dto.setTimestamp(notification.getTimestamp());
                    dto.setType(notification.getType());
                    return dto;
                })
                .collect(Collectors.toList())
        );

        return notifications;
    }


}
