package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.statistic.UsuariosPorDiaDto;
import ps.com.viajeros.dtos.user.*;
import ps.com.viajeros.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    // Registrar un nuevo usuario
    NewUserResponseDto registerUser(NewUserDto user);

    // Obtener un usuario por su ID
    Optional<NewUserResponseDto> getUserById(Long id);

    Optional<EditProfileResponseDto> getUserForEdit(Long id);

    boolean updateUserProfile(Long id, UpdateUserRequestDto updateUserRequestDto);

    // Obtener un usuario por su mail
    UserEntity getUserByEmail(String mail);

    UserEntity getUserByPhone(Long phone);

    // Obtener todos los usuarios
    List<NewUserResponseDto> getAllUsers();

    // Actualizar la información de un usuario
    NewUserResponseDto updateUser(Long id, NewUserDto user);

    // Eliminar un usuario por su ID
    void deleteUser(Long id);


    LoginRequest getUserByEmail(LoginRequest loginRequest);
    void reactivateUser(UserEntity user);

    UserDataDto getDataUserById(Long idUser);

    UserSummaryDto getUserSummary(Long userId);

    List<UsuariosPorDiaDto> getUsuariosNuevosPorDia();
}
