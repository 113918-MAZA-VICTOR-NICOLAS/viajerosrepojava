package ps.com.viajeros.dtos.car;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponseDto {
    private Long idCar;
    private String brand;
    private String model;
    private String patent;
    private String color;
    private String fuel;
    private String kml;
    private boolean gnc;
    private boolean deleted;
}
