package ps.com.viajeros.entities.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.chat.ChatEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "Mensajes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    // Relación con el chat
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private ChatEntity chat;

    // Mensaje de texto
    @Column(name = "contenido", nullable = false, length = 1000)
    private String contenido;
    // Nuevo campo para marcar si el mensaje fue leído

    @JoinColumn(name = "leido", nullable = false)
    private boolean leido = false;  // Inicializado como falso por defecto


    // Usuario que envió el mensaje (chofer o pasajero)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity usuario;

    // Fecha y hora de envío del mensaje
    @Column(name = "fecha_hora_envio", nullable = false)
    private LocalDateTime fechaHoraEnvio;

}
