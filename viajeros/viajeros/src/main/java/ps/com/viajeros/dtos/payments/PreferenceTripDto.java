package ps.com.viajeros.dtos.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenceTripDto {
    private Long idviaje;
    private BigDecimal monto;
    private String title;
    private String description;
    private Long idpasajero;
}
