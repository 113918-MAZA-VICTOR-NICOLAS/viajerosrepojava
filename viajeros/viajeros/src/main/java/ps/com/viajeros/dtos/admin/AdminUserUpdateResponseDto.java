package ps.com.viajeros.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserUpdateResponseDto  {
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String bank;
    private String cbu;
    private String cuil;
    private Long phone;
    private boolean deleted;
    private Boolean blocked;
    private String commentBlocked;
    private String rol;
}