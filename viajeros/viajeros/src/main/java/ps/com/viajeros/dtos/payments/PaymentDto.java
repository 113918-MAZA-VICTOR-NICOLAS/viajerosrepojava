package ps.com.viajeros.dtos.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private String paymentId;
    private String status;
    private String externalReference;
    private String paymentType;
    private String merchantOrderId;
    private Long idViaje;
    private Long idPasajero;
}
