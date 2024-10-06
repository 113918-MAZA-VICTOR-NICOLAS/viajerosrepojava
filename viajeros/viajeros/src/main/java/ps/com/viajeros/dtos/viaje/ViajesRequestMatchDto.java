package ps.com.viajeros.dtos.viaje;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViajesRequestMatchDto {
    private String origin;
    private String destination;
    private Boolean petsAllowed;
    private Boolean smokersAllowed;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHoraInicio;
    private Long equipajePermitido;
    private Long localidadInicioId;
    private Long localidadFinId;
    // Getters y setters
}
