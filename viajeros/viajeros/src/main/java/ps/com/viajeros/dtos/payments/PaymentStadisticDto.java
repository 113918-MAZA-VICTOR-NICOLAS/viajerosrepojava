package ps.com.viajeros.dtos.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStadisticDto {
    Long paidCount;
    Long paidTotal;
    Long pendingCount;
    Long pendingTotal;
    Long rejectedCount;
    Long rejectedTotal;

}
