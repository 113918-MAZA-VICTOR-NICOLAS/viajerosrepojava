package ps.com.viajeros.dtos.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePaymentDto {

    private Long idPayment;
    private String paymentId;
    private String status;
    private String externalReference;
    private String paymentType;
    private String merchantOrderId;
    private Long idPasajero;
    private String nombreCompletoPasajero;
    private String cuilPasajero;
    private String cbuPasajero;
    private LocalDateTime fechaPago;

    // Getters y setters para cada propiedad
}
