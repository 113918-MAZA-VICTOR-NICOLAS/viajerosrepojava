package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.login.LoginResponseDto;
import ps.com.viajeros.services.LoginService;
import ps.com.viajeros.services.UserService;
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserService userService;
    @Override
    public LoginResponseDto loginUser(LoginRequest loginRequest) {
        LoginRequest userSavedInBD = userService.getUserByEmail(loginRequest);
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        // Verificar si el usuario fue encontrado en la base de datos
        if (userSavedInBD != null) {
            // Si el nombre de usuario y la contraseña son correctos
            if (userSavedInBD.getUsername().equals(loginRequest.getUsername().toLowerCase()) &&
                    userSavedInBD.getPassword().equals(loginRequest.getPassword())) {

                loginResponseDto.setLogin(true);
                loginResponseDto.setUserFalse(false);
                loginResponseDto.setPasswordFalse(false);
            } else {
                // Si el nombre de usuario es correcto pero la contraseña es incorrecta
                if (userSavedInBD.getUsername().equals(loginRequest.getUsername().toLowerCase())) {
                    loginResponseDto.setLogin(false);
                    loginResponseDto.setUserFalse(false);
                    loginResponseDto.setPasswordFalse(true);
                } else {
                    // Si el nombre de usuario es incorrecto
                    loginResponseDto.setLogin(false);
                    loginResponseDto.setUserFalse(true);
                    loginResponseDto.setPasswordFalse(false);
                }
            }
        } else {
            // Si el usuario no fue encontrado
            loginResponseDto.setLogin(false);
            loginResponseDto.setUserFalse(true);
            loginResponseDto.setPasswordFalse(false);
        }

        return loginResponseDto;
    }

}
