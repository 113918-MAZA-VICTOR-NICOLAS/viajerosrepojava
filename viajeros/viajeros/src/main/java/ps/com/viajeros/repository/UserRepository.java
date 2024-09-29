package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhone(Long phone);



    UserEntity findByToken(String token);
    // Método para verificar si el email ya existe
    Boolean existsByEmail(String email);

    // Método para verificar si el teléfono ya existe
    Boolean existsByPhone(Long phone);


}
