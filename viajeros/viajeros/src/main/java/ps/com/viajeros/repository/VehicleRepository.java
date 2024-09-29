package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.VehicleEntity;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity,Long> {
    List<VehicleEntity> findByUser(UserEntity user);
}
