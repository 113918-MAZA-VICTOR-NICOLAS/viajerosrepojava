package ps.com.viajeros.dtos.reintegro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.payment.ReintegroStatus;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateReintegroDto {
    private Long idReintegro;
    private ReintegroStatus nuevoEstado;
    private Long idAdministrador;
}
