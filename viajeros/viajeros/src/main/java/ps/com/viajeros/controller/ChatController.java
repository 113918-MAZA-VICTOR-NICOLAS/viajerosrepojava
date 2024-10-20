package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.chat.ChatDto;
import ps.com.viajeros.dtos.chat.IsChoferDto;
import ps.com.viajeros.dtos.chat.MessageDTO;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.chat.MessageEntity;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.ChatRepository;
import ps.com.viajeros.repository.MessageRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.ChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViajeRepository viajeRepository;
    @Autowired
    private MessageRepository messageRepository;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageDTO sendMessage(@DestinationVariable String roomId, MessageDTO messageDTO) {
        // Buscar el chat por ID (roomId = idChat)
        ChatEntity chat = chatRepository.findById(messageDTO.getIdMensaje())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        // Buscar el usuario que envía el mensaje
        UserEntity usuario = userRepository.findById(messageDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la entidad MessageEntity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChat(chat);
        messageEntity.setUsuario(usuario);
        messageEntity.setContenido(messageDTO.getContenido());
        messageEntity.setFechaHoraEnvio(LocalDateTime.now());


        // Guardar el mensaje en la base de datos
        MessageEntity savedMessage = messageRepository.save(messageEntity);

        // Crear y retornar el DTO actualizado con el ID del mensaje guardado
        return new MessageDTO(
                savedMessage.getContenido(),
                savedMessage.getIdMensaje(),  // Aquí retornas el id del mensaje generado
                savedMessage.getUsuario().getIdUser(),
                savedMessage.isLeido()  // Retornar el estado de lectura
        );
    }

    @GetMapping("/getOrCreateChat")
    public ResponseEntity<Long> getOrCreateChat(@RequestParam Long choferId, @RequestParam Long pasajeroId, @RequestParam Long viajeId) {
        // Buscar las entidades del chofer, pasajero y viaje
        UserEntity chofer = userRepository.findById(choferId).orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
        UserEntity pasajero = userRepository.findById(pasajeroId).orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));
        ViajesEntity viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Obtener o crear el ChatEntity
        ChatEntity chat = chatService.findOrCreateChat(chofer, pasajero, viaje);

        // Log para verificar si el ID del chat está siendo generado correctamente
        System.out.println("Chat ID generado: " + chat.getIdChat());

        // Devolver el idChat como respuesta
        return ResponseEntity.ok(chat.getIdChat());
    }

    @GetMapping("/chat/history/{roomId}")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable Long roomId) {
        // Buscar el chat por ID
        ChatEntity chat = chatRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        // Obtener el historial de mensajes para ese chat
        List<MessageEntity> messages = messageRepository.findByChat(chat);

        // Convertir las entidades MessageEntity a MessageDTO
        List<MessageDTO> messageDTOs = messages.stream()
                .map(message -> new MessageDTO(message.getContenido(), message.getIdMensaje(), message.getUsuario().getIdUser(), message.isLeido()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOs);
    }

    @PutMapping("/chat/message/{messageId}/markAsRead")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        message.setLeido(true);
        messageRepository.save(message);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat/message/{chatId}/{userId}")
    public ResponseEntity<IsChoferDto> soyChoferDelChat(@PathVariable Long chatId, @PathVariable Long userId) {
        // Buscar el viaje por el ID
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Obtener el chofer del viaje
        UserEntity chofer = chat.getChofer();

        // Comprobar si el usuario es el chofer
        boolean esChofer = chofer.getIdUser().equals(userId);

        // Obtener el nombre del chofer y del pasajero si es necesario
        String nombreChofer = chofer.getName();
        String nombrePasajero = esChofer ? null : userRepository.findById(userId)
                .map(UserEntity::getName)
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

        // Crear el DTO
        IsChoferDto isChoferDto = IsChoferDto.builder()
                .ischofer(esChofer)
                .idChofer(chofer.getIdUser())
                .nombreChofer(nombreChofer)
                .idPasajero(esChofer ? null : userId)
                .nombrePasajero(nombrePasajero)
                .build();

        // Devolver la respuesta con el DTO
        return ResponseEntity.ok(isChoferDto);
    }
    @GetMapping("/chat/{tripId}/{choferId}/{pasajeroId}")
    public ResponseEntity<Long> getChatIdByViajeChoferPasajero(@PathVariable Long tripId, @PathVariable Long choferId, @PathVariable Long pasajeroId) {
        // Buscar el viaje por ID
        ViajesEntity viaje = viajeRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Buscar el chofer y el pasajero por sus IDs
        UserEntity chofer = userRepository.findById(choferId)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
        UserEntity pasajero = userRepository.findById(pasajeroId)
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

        // Buscar el chat entre el chofer y el pasajero en ese viaje
        ChatEntity chat = chatRepository.findByViajeAndChoferAndPasajero(viaje, chofer, pasajero)
                .orElseThrow(() -> new RuntimeException("No se encontró chat para este viaje, chofer y pasajero"));

        // Devolver el id del chat
        return ResponseEntity.ok(chat.getIdChat());
    }
    // Endpoint para obtener los chats de un usuario ordenados por la fecha del último mensaje
    @GetMapping("/chats/{userId}")
    public ResponseEntity<List<ChatDto>> getChatsByUserId(@PathVariable Long userId) {
        List<ChatDto> chats = chatService.getChatsByUserIdOrderedByDate(userId);
        return ResponseEntity.ok(chats);
    }

}
