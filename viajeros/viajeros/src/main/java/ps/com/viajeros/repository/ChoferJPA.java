package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.ChoferEntity;


@Repository
public interface ChoferJPA extends JpaRepository<ChoferEntity,Long> {
}
