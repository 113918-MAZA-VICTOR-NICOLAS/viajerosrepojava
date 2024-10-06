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
    private boolean mailAlreadyExist = false;
    private boolean phoneAlreadyExist = false;
}
