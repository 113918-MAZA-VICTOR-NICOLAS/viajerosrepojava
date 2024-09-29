package ps.com.viajeros.dtos.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long idCar;
    private String brand;
    private String model;
    private String patent;
    private String color;
    private String fuel;
    private boolean gnc;
    private String kmL;
    private boolean deleted;
}
