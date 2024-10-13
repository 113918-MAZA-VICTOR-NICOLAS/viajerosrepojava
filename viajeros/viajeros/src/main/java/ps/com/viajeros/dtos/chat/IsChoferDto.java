package ps.com.viajeros.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IsChoferDto {
    private boolean ischofer; // Contenido del mensaje
    private Long idChofer;
    private String nombreChofer;// ID del mensaje generado en la base de datos
    private Long idPasajero;   // ID del usuario que envía el mensaje
    private String nombrePasajero;
}
