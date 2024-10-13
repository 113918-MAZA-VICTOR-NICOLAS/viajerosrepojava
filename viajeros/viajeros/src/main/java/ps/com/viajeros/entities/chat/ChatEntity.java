package ps.com.viajeros.entities.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long idChat;

    // Relación con el viaje
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje", nullable = false)
    private ViajesEntity viaje;

    // Relación con el chofer (UserEntity)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chofer", nullable = false)
    private UserEntity chofer;

    // Relación con el pasajero (UserEntity)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajero", nullable = false)
    private UserEntity pasajero;

    // Lista de mensajes en el chat
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> mensajes;


    // Método para obtener el último mensaje del chat
    @Transient
    public MessageEntity getLastMessage() {
        return mensajes.stream()
                .max(Comparator.comparing(MessageEntity::getFechaHoraEnvio))
                .orElse(null);
    }
}
