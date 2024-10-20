package ps.com.viajeros.dtos.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuariosPorDiaDto {
    private String fecha;
    private long cantidadUsuarios;

    // Getters y Setters
}

