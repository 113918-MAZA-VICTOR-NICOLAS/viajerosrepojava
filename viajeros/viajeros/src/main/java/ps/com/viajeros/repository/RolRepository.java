package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.user.RolEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {
}
