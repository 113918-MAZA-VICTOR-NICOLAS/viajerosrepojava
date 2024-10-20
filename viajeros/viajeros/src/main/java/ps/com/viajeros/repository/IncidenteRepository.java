package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.viajes.IncidenteEntity;
@Repository
public interface IncidenteRepository extends JpaRepository<IncidenteEntity, Long> {
}
