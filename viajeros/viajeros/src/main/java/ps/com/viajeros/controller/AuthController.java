package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.login.LoginResponseDto;
import ps.com.viajeros.dtos.user.NewUserResponseDto;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.security.services.JwtService;
import ps.com.viajeros.services.LoginService;
import ps.com.viajeros.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validar las credenciales del usuario
        LoginResponseDto loginResponseDto = loginService.loginUser(loginRequest);

        if (loginResponseDto.getLogin()) {
            // Genera el token JWT
            String token = jwtService.generarToken(loginRequest.getUsername());

            // Obtén los detalles del usuario autenticado (por ejemplo, id y nombre)
            UserEntity usuario = userService.getUserByEmail(loginRequest.getUsername());

            // Envuelve el token y otros detalles en un objeto JSON
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", usuario.getIdUser());
            response.put("name", usuario.getName());

            // Devuelve el objeto JSON con el token y los datos del usuario
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
        }
    }





}