package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.viajes.ViajesEntity;
@Repository
public interface ViajeRepository extends JpaRepository<ViajesEntity, Long> {
}
