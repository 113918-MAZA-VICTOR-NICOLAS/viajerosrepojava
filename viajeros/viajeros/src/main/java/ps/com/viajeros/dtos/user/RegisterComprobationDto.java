package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterComprobationDto {
    private Boolean mailAlreadyExist = false;
    private Boolean phoneAlreadyExist = false;
}
