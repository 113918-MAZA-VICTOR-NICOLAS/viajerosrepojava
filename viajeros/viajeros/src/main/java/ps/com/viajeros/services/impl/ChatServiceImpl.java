package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.chat.ChatDto;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.chat.MessageEntity;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.ChatRepository;
import ps.com.viajeros.repository.MessageRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.ChatService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {


    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para enviar un mensaje en un chat
    public MessageEntity sendMessage(Long chatId, Long userId, String messageContent) throws Exception {
        // Buscar el chat por ID
        Optional<ChatEntity> chatOpt = chatRepository.findById(chatId);
        if (chatOpt.isEmpty()) {
            throw new Exception("Chat no encontrado.");
        }
        ChatEntity chat = chatOpt.get();

        // Buscar el usuario que envía el mensaje (puede ser el chofer o el pasajero)
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        UserEntity user = userOpt.get();

        // Crear un nuevo mensaje
        MessageEntity message = new MessageEntity();
        message.setChat(chat);
        message.setUsuario(user);
        message.setContenido(messageContent);
        message.setFechaHoraEnvio(LocalDateTime.now());

        // Guardar el mensaje en la base de datos
        return messageRepository.save(message);
    }

    @Override
    public ChatEntity findOrCreateChat(UserEntity chofer, UserEntity pasajero, ViajesEntity viaje) {
        // Verificar si ya existe un chat entre este chofer y pasajero en este viaje
        Optional<ChatEntity> existingChat = chatRepository.findByChoferAndPasajeroAndViaje(chofer, pasajero, viaje);

        if (existingChat.isPresent()) {
            System.out.println("Chat ya existente, idChat: " + existingChat.get().getIdChat());
            return existingChat.get();
        } else {
            // Crear un nuevo ChatEntity si no existe
            ChatEntity chat = new ChatEntity();
            chat.setChofer(chofer);
            chat.setPasajero(pasajero);
            chat.setViaje(viaje);
            ChatEntity savedChat = chatRepository.save(chat);
            System.out.println("Nuevo chat creado, idChat: " + savedChat.getIdChat());
            return savedChat;
        }
    }

    @Override
    public List<ChatDto> getChatsByUserIdOrderedByDate(Long userId) {
        // Buscar el usuario correspondiente por ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar todos los chats donde el usuario sea el chofer o el pasajero
        List<ChatEntity> chats = chatRepository.findByChoferOrPasajero(user, user);

        // Ordenar los chats por la fecha del último mensaje y mapear a ChatDto
        return chats.stream()
                .map(chat -> {
                    // Obtener el último mensaje (si no tienes el método getLastMessage)
                    MessageEntity lastMessage = chat.getMensajes().stream()
                            .max(Comparator.comparing(MessageEntity::getFechaHoraEnvio))
                            .orElse(null);

                    // Construir ChatDto usando el builder (si estás usando @Builder en ChatDto)
                    return ChatDto.builder()
                            .id(chat.getIdChat())
                            .name(chat.getChofer().getName() + " - " + chat.getPasajero().getName())  // Nombre del chat
                            .lastMessage(lastMessage != null ? lastMessage.getContenido() : "Sin mensajes")
                            .lastMessageDate(lastMessage != null ? lastMessage.getFechaHoraEnvio() : null)
                            .idTrip(chat.getViaje().getIdViaje().toString())  // ID del viaje
                            .idPassenger(chat.getPasajero().getIdUser().toString())
                            .build();
                })
                .sorted(Comparator.comparing(ChatDto::getLastMessageDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())  // Ordenar por fecha del último mensaje
                .collect(Collectors.toList());
    }


    public List<MessageEntity> getChatHistory(Long chatId) {
        Optional<MessageEntity> optionalMessage = messageRepository.findById(chatId);
        // Devuelve una lista con un solo elemento
        // Devuelve una lista vacía si no se encuentra el mensaje
        return optionalMessage.map(Collections::singletonList).orElse(Collections.emptyList());
    }


}
