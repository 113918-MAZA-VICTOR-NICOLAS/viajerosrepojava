package ps.com.viajeros.dtos.valuations;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ps.com.viajeros.entities.user.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuationRequestDto {
    private Long idTrip;      // ID del viaje
    private Long idUserValuated;      // ID del usuario que es calificado (pasajero)
    private Long idUserWhoValuated;
    private Long rating;      // La calificación dada
    private String comments;  // Los comentarios adicionales, si es necesario
}
