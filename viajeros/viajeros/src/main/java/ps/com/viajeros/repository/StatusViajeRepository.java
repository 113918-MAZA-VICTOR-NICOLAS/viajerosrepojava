package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;
@Repository
public interface StatusViajeRepository extends JpaRepository<StatusEntity, Long>{
}