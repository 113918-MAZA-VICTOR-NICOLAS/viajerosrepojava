package ps.com.viajeros.dtos.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDriverPaymentDto {
    private Long idPago;
    private String estado; // Estado del pago del chofer (PENDIENTE, PAGADO, etc.)
    private Long idTransferenciaChofer; // ID de la transferencia al chofer
}