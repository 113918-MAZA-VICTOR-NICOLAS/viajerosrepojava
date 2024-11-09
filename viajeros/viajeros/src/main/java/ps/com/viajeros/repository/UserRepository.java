package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.dtos.statistic.UsuariosPorDiaDto;
import ps.com.viajeros.entities.user.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {


    // Devolver una lista de usuarios con el mismo email
    List<UserEntity> findByEmail(String email);

    // Devolver una lista de usuarios con el mismo teléfono
    List<UserEntity> findByPhone(Long phone);


    UserEntity findByToken(String token);
    // Método para verificar si el email ya existe
    Boolean existsByEmail(String email);

    // Método para verificar si el teléfono ya existe
    Boolean existsByPhone(Long phone);
    @Query("SELECT new ps.com.viajeros.dtos.statistic.UsuariosPorDiaDto(TO_CHAR(u.registrationDate, 'YYYY-MM-DD'), COUNT(u)) " +
            "FROM UserEntity u " +
            "GROUP BY TO_CHAR(u.registrationDate, 'YYYY-MM-DD') " +
            "ORDER BY TO_CHAR(u.registrationDate, 'YYYY-MM-DD') ASC")
    List<UsuariosPorDiaDto> getUsuariosNuevosPorDia();
    List<UserEntity> findAllByDeletedFalse();

}
