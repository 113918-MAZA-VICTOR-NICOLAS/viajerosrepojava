package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ps.com.viajeros.dtos.car.VehicleDTO;
import ps.com.viajeros.dtos.pet.PetDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataDto {
    private Long idUser;
    private String name;
    private String lastname;
    private String email;
    private String bank;
    private String cbu;
    private String cuil;
    private Long phone;
    private boolean deleted;
    private String profileImage;
    private LocalDateTime registrationDate;
    private List<VehicleDTO> vehicles;
    private List<PetDto> pets;
    private List<ValuationDTO> valuations;

    // Getters and Setters
}
