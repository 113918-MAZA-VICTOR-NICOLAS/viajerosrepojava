package ps.com.viajeros.dtos.viaje.incidentes;

import lombok.*;
import ps.com.viajeros.entities.viajes.EstadoResolucion;
import ps.com.viajeros.entities.viajes.TipoIncidente;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidenteDto {
    private Long idIncidente;
    private Long viajeId;
    private String descripcion;
    private TipoIncidente tipoIncidente;
    private LocalDateTime fechaIncidente;
    private Boolean isPasajero;
    private Long denunciadoId;
    private Long reportadoPorId;
    private EstadoResolucion estadoResolucion;
    private String resolucion;
    private LocalDateTime fechaResolucion;
}