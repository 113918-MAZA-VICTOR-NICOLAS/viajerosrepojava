package ps.com.viajeros.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViajeDto {
    private Long id;
    private String driverName;
    private List<String> passengers; // Lista de nombres de pasajeros
    private String origin;
    private String destination;
    private LocalDateTime startDate;
    private String status; // PENDING, FINISHED, etc.

    // Getters y Setters
}
