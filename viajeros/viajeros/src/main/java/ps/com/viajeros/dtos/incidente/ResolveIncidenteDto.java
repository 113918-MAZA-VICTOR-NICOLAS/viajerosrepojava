package ps.com.viajeros.dtos.incidente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ps.com.viajeros.entities.viajes.EstadoResolucion;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResolveIncidenteDto {
    private Long idUser;  // ID del administrador que resuelve el incidente
    private String resolucion;  // Comentario de resolución
    private EstadoResolucion estadoResolucion;  // Estado de la resolución

}