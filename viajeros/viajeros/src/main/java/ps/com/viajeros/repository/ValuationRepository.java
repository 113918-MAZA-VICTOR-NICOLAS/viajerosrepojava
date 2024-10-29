package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.ValuationEntity;

import java.util.List;

@Repository
public interface ValuationRepository extends JpaRepository<ValuationEntity,Long> {
    @Query("SELECT AVG(v.rating) FROM ValuationEntity v WHERE v.userValuated.idUser = :userId AND v.idTrip IN (SELECT ve.idViaje FROM ViajesEntity ve WHERE ve.chofer.idUser = :userId)")
    Long getAverageRatingByUserIdAndAsDriver(@Param("userId") Long userId);

    @Query("SELECT AVG(v.rating) FROM ValuationEntity v WHERE v.userValuated.idUser = :userId AND v.idTrip IN (SELECT ve.idViaje FROM ViajesEntity ve JOIN ve.pasajeros p WHERE p.idUser = :userId)")
    Long getAverageRatingByUserIdAndAsPassenger(@Param("userId") Long userId);


    List<ValuationEntity> findByIdTrip(Long idTrip);

    @Query("SELECT COUNT(v) > 0 FROM ValuationEntity v WHERE v.idTrip = :tripId AND v.userWhoValuated.idUser = :passengerId AND v.userValuated.idUser = (SELECT ve.chofer.idUser FROM ViajesEntity ve WHERE ve.idViaje = :tripId)")
    boolean existsByTripIdAndPassengerIdAndValuatedIsDriver(@Param("tripId") Long tripId, @Param("passengerId") Long passengerId);

}
