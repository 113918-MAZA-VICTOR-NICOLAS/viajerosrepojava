package ps.com.viajeros.dtos.viaje;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengersDto {
    private Long id;
    private String nombre;
    private String apellido;
}
