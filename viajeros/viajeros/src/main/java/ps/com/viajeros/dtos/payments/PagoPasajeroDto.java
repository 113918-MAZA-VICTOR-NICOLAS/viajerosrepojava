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
public class PagoPasajeroDto {
    private Long idPayment;
    private Long idViaje;
    private String nombrePasajero;
    private BigDecimal monto;
    private String estadoPagoPasajero;
    private String nombreChofer;
    private String cbuChofer;
    private String bancoChofer;
    private String estadoPagoChofer;
}