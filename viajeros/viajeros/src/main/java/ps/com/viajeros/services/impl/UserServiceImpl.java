package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.car.VehicleDTO;
import ps.com.viajeros.dtos.login.LoginRequest;
import ps.com.viajeros.dtos.pet.PetDto;
import ps.com.viajeros.dtos.user.*;
import ps.com.viajeros.entities.UserEntity;
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
        List<UserEntity> userEntities = userRepository.findByPhone(phone);

        // Si no hay usuarios o el primero no está eliminado, devolver el primero de la lista
        if (!userEntities.isEmpty()) {
            return userEntities.get(0);
        }

        throw new RuntimeException("No user found with phone: " + phone);
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
        // Buscar todos los usuarios con el mismo email
        List<UserEntity> users = userRepository.findByEmail(loginRequest.getUsername());

        // Recorrer la lista de usuarios para encontrar el primero que no esté eliminado
        for (UserEntity user : users) {
            if (!user.isDeleted()) {
                LoginRequest userSavedInBD = new LoginRequest();
                userSavedInBD.setUsername(user.getEmail()); // Usamos el email del usuario
                userSavedInBD.setPassword(user.getPassword()); // Asumiendo que guardas la contraseña (no recomendable)
                return userSavedInBD; // Retornamos el primer usuario activo encontrado
            }
        }

        // Si no se encuentra ningún usuario activo, devolver null
        return null;
    }


    @Override
    public UserEntity getUserByEmail(String mail) {
        List<UserEntity> userEntities = userRepository.findByEmail(mail);

        // Si no hay usuarios o el primero no está eliminado, devolver el primero de la lista
        if (!userEntities.isEmpty()) {
            return userEntities.get(0);
        }

        throw new RuntimeException("No user found with email: " + mail);
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

    @Override
    public UserDataDto getDataUserById(Long idUser) {
        UserEntity userEntity = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToDTO(userEntity);
    }

    private UserDataDto convertToDTO(UserEntity userEntity) {
        List<VehicleDTO> vehicles = userEntity.getVehicles()
                .stream()
                .map(vehicle -> new VehicleDTO(vehicle.getIdCar(), vehicle.getBrand(), vehicle.getModel(),
                        vehicle.getPatent(), vehicle.getColor(), vehicle.getFuel(), vehicle.isGnc(),
                        vehicle.getKmL(), vehicle.isDeleted()))
                .collect(Collectors.toList());

        List<PetDto> pets = userEntity.getPets()
                .stream()
                .filter(pet -> !pet.isDeleted())
                .map(pet -> new PetDto(pet.getIdPet(), pet.getName(), false, pet.isCanil(),
                        pet.getSize().getSizeName(), pet.getType().getTypeName()))
                .collect(Collectors.toList());

        List<ValuationDTO> valuations = userEntity.getValuations()
                .stream()
                .map(valuation -> new ValuationDTO(valuation.getIdValuation(), valuation.getIdTrip(),
                        valuation.getComments(), valuation.getRating()))
                .collect(Collectors.toList());

        return new UserDataDto(userEntity.getIdUser(), userEntity.getName(), userEntity.getLastname(),
                userEntity.getEmail(), userEntity.getBank(), userEntity.getCbu(), userEntity.getCuil(),
                userEntity.getPhone(), userEntity.isDeleted(), userEntity.getProfileImage(),
                userEntity.getRegistrationDate(), vehicles, pets, valuations);
    }

}
