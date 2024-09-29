package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.login.LoginResponseDto;

@Service
public interface LoginService {
    LoginResponseDto loginUser(LoginRequest loginRequest);
}
