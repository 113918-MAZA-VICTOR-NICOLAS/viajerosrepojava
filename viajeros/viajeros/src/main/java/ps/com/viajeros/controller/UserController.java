package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.statistic.UsuariosPorDiaDto;
import ps.com.viajeros.dtos.user.*;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.impl.EmailService;
import ps.com.viajeros.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    // Registrar un nuevo usuario (POST /api/users)

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserDto newUserDto) {
        RegisterComprobationDto comprobationDto = new RegisterComprobationDto(false, false); // Inicializamos el DTO

        // Buscar todos los usuarios con el mismo email
        List<UserEntity> userEntitiesByEmail = userRepository.findByEmail(newUserDto.getEmail().toLowerCase());
        boolean canRegisterByEmail = true;

        for (UserEntity user : userEntitiesByEmail) {
            if (!user.isDeleted()) {
                comprobationDto.setMailAlreadyExist(true);
                canRegisterByEmail = false;
                break;  // Si encontramos un email que no está eliminado, salimos del bucle
            }
        }

        // Buscar todos los usuarios con el mismo teléfono
        List<UserEntity> userEntitiesByPhone = userRepository.findByPhone(newUserDto.getPhone());
        boolean canRegisterByPhone = true;

        for (UserEntity user : userEntitiesByPhone) {
            if (!user.isDeleted()) {
                comprobationDto.setPhoneAlreadyExist(true);
                canRegisterByPhone = false;
                break;  // Si encontramos un teléfono que no está eliminado, salimos del bucle
            }
        }

        // Si alguno de los dos no permite el registro, devolver un error
        if (!canRegisterByEmail || !canRegisterByPhone) {
            return ResponseEntity.badRequest().body(comprobationDto);
        }

        // Si todos los emails y teléfonos están eliminados, permitir el registro
        NewUserResponseDto newUser = userService.registerUser(newUserDto);

        // Devolver un objeto JSON con el mensaje de éxito
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado con éxito");

        return ResponseEntity.ok(response);
    }


    // Obtener un usuario por ID (GET /api/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<NewUserResponseDto> getUserById(@PathVariable Long id) {
        Optional<NewUserResponseDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)  // Código 200: éxito
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Código 404: no encontrado
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        boolean isUpdated = userService.updateUserProfile(id, updateUserRequestDto);
        if (isUpdated) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<EditProfileResponseDto> getUserEditById(@PathVariable Long id) {
        Optional<EditProfileResponseDto> user = userService.getUserForEdit(id);
        return user.map(ResponseEntity::ok)  // Código 200: éxito
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Código 404: no encontrado
    }

    // Obtener todos los usuarios (GET /api/users)
    @GetMapping
    public ResponseEntity<List<NewUserResponseDto>> getAllUsers() {
        List<NewUserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);  // Código 200: éxito
    }


    // Actualizar un usuario existente (PUT /api/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<NewUserResponseDto> updateUser(@PathVariable Long id, @RequestBody NewUserDto newUserDto) {
        try {
            NewUserResponseDto updatedUser = userService.updateUser(id, newUserDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);  // Código 200: éxito
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Código 404: no encontrado
        }
    }

    // Eliminar un usuario por ID (DELETE /api/users/{id})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Código 204: eliminado con éxito, sin contenido
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Código 404: no encontrado
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        // Generar OTP
        String otp = generateOtp();  // Un método que genera un OTP

        // Enviar OTP al email
        emailService.sendOtpEmail(email, otp);

        return new ResponseEntity<>("OTP enviado con éxito", HttpStatus.OK);
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // OTP de 6 dígitos
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String token, @RequestBody Map<String, String> payload) {
        String newPassword = payload.get("password"); // Asegúrate de que el nombre de la propiedad coincida
        if (emailService.isValidToken(token)) {
            emailService.updatePassword(token, newPassword);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Contraseña actualizada con éxito.");
            return ResponseEntity.ok(response); // Devuelve un objeto JSON
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token inválido."));
        }
    }



    @PostMapping("/recovery-mail")
    public ResponseEntity<Map<String, String>> sentMailRecovery(@RequestParam String identifier) {
        UserEntity user;
        Map<String, String> response = new HashMap<>();

        // Verifica si es un email o un teléfono usando expresiones regulares
        if (isEmail(identifier)) {
            user = userService.getUserByEmail(identifier);
        } else if (isPhoneNumber(identifier)) {
            user = userService.getUserByPhone(Long.parseLong(identifier));
        } else {
            response.put("error", "Formato de identificador no válido. Debe ser un correo electrónico o un número de teléfono.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (user == null) {
            response.put("error", "El correo o teléfono no está registrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        emailService.sendResetEmail(user.getEmail());
        response.put("message", "Correo de recuperación enviado.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("dataUser/{id}")
    public ResponseEntity<UserDataDto> getDataUserById(@PathVariable Long id) {
        UserDataDto userDTO = userService.getDataUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{userId}/summary")
    public ResponseEntity<UserSummaryDto> getUserSummary(@PathVariable Long userId) {
        UserSummaryDto userSummary = userService.getUserSummary(userId);
        return ResponseEntity.ok(userSummary);
    }

    // Verifica si el identificador es un email
    private boolean isEmail(String identifier) {
        // Expresión regular para emails
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return identifier.matches(emailRegex);
    }

    // Verifica si el identificador es un número de teléfono (solo dígitos)
    private boolean isPhoneNumber(String identifier) {
        // Expresión regular para números de teléfono (puede modificarse según el formato esperado)
        String phoneRegex = "^[0-9]{7,15}$"; // Asume que el teléfono tiene entre 7 y 15 dígitos
        return identifier.matches(phoneRegex);
    }
    @GetMapping("/nuevos-por-dia")
    public ResponseEntity<List<UsuariosPorDiaDto>> getUsuariosNuevosPorDia() {
        List<UsuariosPorDiaDto> usuariosNuevos = userService.getUsuariosNuevosPorDia();
        return ResponseEntity.ok(usuariosNuevos);
    }

}