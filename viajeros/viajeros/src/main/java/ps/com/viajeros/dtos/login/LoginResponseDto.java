package ps.com.viajeros.dtos.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.print.Book;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDto {
    Boolean login;
    Boolean userFalse;
    Boolean passwordFalse;
    Long trying;

}
