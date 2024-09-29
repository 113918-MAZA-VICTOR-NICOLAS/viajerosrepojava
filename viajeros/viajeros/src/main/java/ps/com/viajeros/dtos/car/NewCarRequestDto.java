package ps.com.viajeros.dtos.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCarRequestDto {
    private Long userId;
    private String brand;
    private String model;
    private String patent;
    private String color;
    private String fuel;
    private String kml;
    private boolean gnc;

}
