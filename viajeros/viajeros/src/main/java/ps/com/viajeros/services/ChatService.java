package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.chat.ChatDto;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.chat.MessageEntity;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;

@Service
public interface ChatService {
    MessageEntity sendMessage(Long chatId, Long userId, String messageContent) throws Exception;
    ChatEntity findOrCreateChat(UserEntity chofer, UserEntity pasajero, ViajesEntity viaje);
    List<ChatDto> getChatsByUserIdOrderedByDate(Long userId);
}
