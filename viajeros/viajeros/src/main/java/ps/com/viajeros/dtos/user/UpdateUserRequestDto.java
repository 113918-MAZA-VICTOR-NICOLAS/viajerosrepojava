package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
    private String name;
    private String lastname;
    private String email;
    private String bank;
    private String cbu;
    private String cuil;
    private Long phone;
}
