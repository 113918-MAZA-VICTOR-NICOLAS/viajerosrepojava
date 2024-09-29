package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Viajeros.com Verificación de OTP");
        message.setText(otp);
        mailSender.send(message);
    }

    public void sendResetEmail(String email) {
        UserEntity user = userService.getUserByEmail(email);
        String token = generateResetToken();
        user.setToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        userRepository.save(user);

        String resetLink = "http://localhost:4200/renewPass?token=" + token;
        // Envía el correo usando tu EmailService
        String emailBody = "Haga clic en el siguiente enlace para restablecer su contraseña: " + resetLink +
                "\nEl token tiene un tiempo de expiracion de una hora." +
                "\nSi no solicitó este correo, ignore este mensaje.";
        sendOtpEmail(email, emailBody);
    }

    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token) {
        UserEntity user = userRepository.findByToken(token); // Asumiendo que tienes este método en el repositorio
        if (user != null) {
            // Verifica si el token ha expirado
            return LocalDateTime.now().isBefore(user.getTokenExpiration());
        }
        return false; // Token no encontrado o expirado
    }

    public void updatePassword(String token, String newPassword) {
        UserEntity user = userRepository.findByToken(token);
        if (user != null) {
            user.setPassword(newPassword); // Asegúrate de encriptar la contraseña antes de guardarla
            user.setToken(null); // Opcional: Elimina el token después de usarlo
            user.setTokenExpiration(null);
            userRepository.save(user);
        }
    }


}
