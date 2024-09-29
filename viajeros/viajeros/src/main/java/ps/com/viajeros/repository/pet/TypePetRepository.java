package ps.com.viajeros.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.pet.TypePetEntity;

@Repository
public interface TypePetRepository extends JpaRepository<TypePetEntity,Long> {
}
