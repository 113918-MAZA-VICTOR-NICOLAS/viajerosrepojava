package ps.com.viajeros.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.pet.PetEntity;

import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<PetEntity,Long> {
    List<PetEntity> findByUser(UserEntity user);
}
