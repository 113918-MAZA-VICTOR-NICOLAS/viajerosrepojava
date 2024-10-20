package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusViajeRepository extends JpaRepository<StatusEntity, Long>{
    Optional<StatusEntity> findByName(String statusName);
}
