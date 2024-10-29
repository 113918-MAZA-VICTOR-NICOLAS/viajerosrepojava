package ps.com.viajeros.dtos.incidente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentesForAdminDto {
    private Long idIncidente;
    private Long idDenunciado;
    private String nombreCompletoDenunciado;
    private Long idDenunciante;
    private String nombreCompletoDenunciante;
    private String comentarios;
    private String estado;
    private String tipoIncidente;
    private LocalDateTime fechaIncidente;
    private Boolean isPasajero;
    private String resolucion;
    private LocalDateTime fechaResolucion;
}