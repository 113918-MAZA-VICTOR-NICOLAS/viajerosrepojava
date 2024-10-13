package ps.com.viajeros.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private String contenido; // Contenido del mensaje
    private Long idMensaje;   // ID del mensaje generado en la base de datos
    private Long idUsuario;   // ID del usuario que envía el mensaje
    private boolean leido;  // Nuevo campo para saber si el mensaje fue leído
}
