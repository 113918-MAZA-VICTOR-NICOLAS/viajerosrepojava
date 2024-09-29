package ps.com.viajeros.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditProfileResponseDto {
    private String name;
    private String lastname;
    private Long phone;
    private String cuil;
    private String bank;
    private String cbu;
}
