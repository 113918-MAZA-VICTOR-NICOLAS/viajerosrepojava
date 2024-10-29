package ps.com.viajeros.dtos.reintegro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReintegroDto {
    private Long idReintegro;
    private String status;
    private Long adminReintegroId; // ID del administrador que gestionó el reintegro
    private String nombreAdminReintegro;
    private LocalDateTime fechaReintegro;
    private String reintegroMotivo;
    private Long paymentId; // ID del pago asociado
}
