package ps.com.viajeros.dtos.valuations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuationResponseDto {
    private Long idValuation;  // El ID de la valoración generada
    private Long idTrip;       // El ID del viaje
    private Long userValuated;       // El ID del usuario que fue calificado
    private Long userWhoValuated;
    private Long rating;       // La calificación dada
    private String comments;   // Los comentarios adicionales
}
