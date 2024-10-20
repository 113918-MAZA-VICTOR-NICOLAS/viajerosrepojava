package ps.com.viajeros.dtos.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViajesPorMesDto {
    private String mes;
    private long cantidadViajesFinalizados;

    // Getters y Setters
}
