package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.user.*;
import ps.com.viajeros.entities.UserEntity;
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
        RegisterComprobationDto comprobationDto = new RegisterComprobationDto(false, false);  // Asumiendo que el constructor inicializa los booleanos a false

        Optional<UserEntity> userEntityMail = userRepository.findByEmail(newUserDto.getEmail().toLowerCase());


        if (userEntityMail.isPresent()) {

            if (userEntityMail.get().isDeleted()) {
                userService.reactivateUser(userEntityMail.get());
            }
            comprobationDto.setMailAlreadyExist(true);
        }

        Optional<UserEntity> userEntityPhone = userRepository.findByPhone(newUserDto.getPhone());


        if(userEntityMail.isPresent()){
            if (userEntityPhone.get().isDeleted()) {
                userService.reactivateUser(userEntityMail.get());
            }
            comprobationDto.setPhoneAlreadyExist(true);
        }


        // Si el email o el teléfono ya existen, devolver el DTO con un código de error
        if (comprobationDto.getMailAlreadyExist() || comprobationDto.getPhoneAlreadyExist()) {
            return new ResponseEntity<>(comprobationDto, HttpStatus.CONFLICT);  // Código 409: conflicto
        }

        // Si no existen, proceder con el registro
        NewUserResponseDto newUser = userService.registerUser(newUserDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);  // Código 201: creado con éxito
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


}