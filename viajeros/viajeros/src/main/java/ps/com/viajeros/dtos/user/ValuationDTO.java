package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuationDTO {
    private Long idValuation;
    private Long idTrip;
    private String comments;
    private Long rating;
}
