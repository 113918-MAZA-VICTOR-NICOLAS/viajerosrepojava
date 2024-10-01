package ps.com.viajeros.dtos.viaje;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewRequestViajeDto {

    private Long idVehiculo;
    private Long idChofer;
    private Long localidadInicioId;
    private Long localidadFinId;
    private LocalDateTime fechaHoraInicio;
    private BigDecimal gastoTotal;
    private int asientosDisponibles;
    private boolean aceptaMascotas;
    private boolean aceptaFumar;

}