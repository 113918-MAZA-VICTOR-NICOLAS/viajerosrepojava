package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryDto {
    private Long completedTrips;
    private Long pendingTrips;
    private Long averageRating;
    private String fullName;


}