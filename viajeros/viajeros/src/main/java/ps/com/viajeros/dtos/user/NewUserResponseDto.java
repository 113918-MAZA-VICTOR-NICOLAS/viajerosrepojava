package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserResponseDto {
    private Long idUser;
    private String name;
    private String email;
    private Long phone;
}
