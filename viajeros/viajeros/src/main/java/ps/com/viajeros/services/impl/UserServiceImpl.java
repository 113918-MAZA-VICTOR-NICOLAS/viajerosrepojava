package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.user.EditProfileResponseDto;
import ps.com.viajeros.dtos.user.NewUserResponseDto;
import ps.com.viajeros.dtos.user.UpdateUserRequestDto;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.dtos.user.NewUserDto;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public NewUserResponseDto registerUser(NewUserDto newUserDto) {
        // Convertir UserViajeros (DTO de entrada) a UserEntity (entidad)
        UserEntity userEntity = new UserEntity();
        userEntity.setName(newUserDto.getName());
        userEntity.setEmail(newUserDto.getEmail());
        userEntity.setPhone(newUserDto.getPhone());
        userEntity.setPassword(newUserDto.getPassword());
        userEntity.setRegistrationDate(LocalDateTime.now());

        // Guardar en la base de datos
        UserEntity savedUser = userRepository.save(userEntity);

        // Convertir UserEntity a UserResponseDto (DTO de salida)
        return new NewUserResponseDto(savedUser.getIdUser(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhone());
    }
    // Verificar si el email ya existe

    @Override
    public Optional<NewUserResponseDto> getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(user -> new NewUserResponseDto(user.getIdUser(), user.getName(), user.getEmail(), user.getPhone()));
    }

    @Override
    public UserEntity getUserByEmail(String mail) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(mail);
        return userEntity.get();

    }
    @Override
    public Optional<EditProfileResponseDto> getUserForEdit(Long id) {
        UserEntity userEntity = userRepository.getReferenceById(id);

        if (userEntity != null) {
            EditProfileResponseDto editProfileResponseDto = new EditProfileResponseDto();
            editProfileResponseDto.setName(userEntity.getName());
            editProfileResponseDto.setLastname(userEntity.getLastname());
            editProfileResponseDto.setPhone(userEntity.getPhone());
            editProfileResponseDto.setCuil(userEntity.getCuil());
            editProfileResponseDto.setCbu(userEntity.getCbu());
            editProfileResponseDto.setBank(userEntity.getBank());

            // Envuelve el objeto en Optional
            return Optional.of(editProfileResponseDto);
        }

        // Retorna un Optional vacío si el usuario no existe
        return Optional.empty();
    }
    @Override
    public UserEntity getUserByPhone(Long phone) {
        Optional<UserEntity> userEntity = userRepository.findByPhone(phone);
        return userEntity.get();
    }

    @Override
    public List<NewUserResponseDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> new NewUserResponseDto(user.getIdUser(), user.getName(), user.getEmail(), user.getPhone()))
                .collect(Collectors.toList());
    }
@Override
    public void reactivateUser(UserEntity user) {
        user.setDeleted(false);
        userRepository.save(user);
    }


    @Override
    public NewUserResponseDto updateUser(Long id, NewUserDto newUserDto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setName(newUserDto.getName());
            userEntity.setEmail(newUserDto.getEmail());
            userEntity.setPhone(newUserDto.getPhone());

            UserEntity updatedUser = userRepository.save(userEntity);
            return new NewUserResponseDto(updatedUser.getIdUser(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getPhone());
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            UserEntity userEntity = userRepository.findById(id).get();
            userEntity.setDeleted(true);
            userRepository.save(userEntity);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public LoginRequest getUserByEmail(LoginRequest loginRequest) {
        // Aquí debes buscar el usuario por el email (username en este caso)
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.getUsername());
        if (user.isPresent() && !user.get().isDeleted()) {
            LoginRequest userSavedInBD = new LoginRequest();
            userSavedInBD.setUsername(user.get().getEmail()); // o el campo que uses
            userSavedInBD.setPassword(user.get().getPassword()); // Asumiendo que guardas la contraseña (no es recomendable)
            return userSavedInBD;
        }
        return null;
    }

    @Override
    public boolean updateUserProfile(Long id, UpdateUserRequestDto updateUserRequestDto) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Actualizar solo los campos no nulos
            if (updateUserRequestDto.getName() != null) {
                user.setName(updateUserRequestDto.getName());
            }
            if (updateUserRequestDto.getLastname() != null) {
                user.setLastname(updateUserRequestDto.getLastname());
            }
            if (updateUserRequestDto.getEmail() != null) {
                user.setEmail(updateUserRequestDto.getEmail());
            }
            if (updateUserRequestDto.getBank() != null) {
                user.setBank(updateUserRequestDto.getBank());
            }
            if (updateUserRequestDto.getCbu() != null) {
                user.setCbu(updateUserRequestDto.getCbu());
            }
            if (updateUserRequestDto.getCuil() != null) {
                user.setCuil(updateUserRequestDto.getCuil());
            }
            if (updateUserRequestDto.getPhone() != null) {
                user.setPhone(updateUserRequestDto.getPhone());
            }

            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

}
