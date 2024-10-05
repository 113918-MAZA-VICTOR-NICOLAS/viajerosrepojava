package ps.com.viajeros.dtos.viaje;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViajesRequestMatchDto {
    private String origin;
    private String destination;
    private Boolean petsAllowed;
    private Boolean smokersAllowed;

    // Getters y setters
}
