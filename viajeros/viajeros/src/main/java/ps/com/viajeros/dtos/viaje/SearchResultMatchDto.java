package ps.com.viajeros.dtos.viaje;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultMatchDto {
    private Long tripId;
    private String origin;
    private String destination;
    private int availableSeats;
    private LocalDateTime date;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String estimatedDuration;
    private boolean petsAllowed;
    private boolean smokersAllowed;
    private String vehicleName;
    private double driverRating;
    private String driverName;
    private Long driverId;
    private String status;

    // Getters y setters
}

