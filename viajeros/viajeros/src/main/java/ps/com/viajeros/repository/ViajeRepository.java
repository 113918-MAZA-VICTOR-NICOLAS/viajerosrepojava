package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<ViajesEntity, Long> {


    // Buscar por localidades de inicio y fin, y filtrar por estado
    List<ViajesEntity> findByLocalidadInicioAndLocalidadFinAndEstado(LocalidadEntity localidadInicio, LocalidadEntity localidadFin, StatusEntity estado);

    // Buscar por localidad de inicio y filtrar por estado
    List<ViajesEntity> findByLocalidadInicioAndEstado(LocalidadEntity localidadInicio, StatusEntity estado);

    List<ViajesEntity> findByEstado(StatusEntity status);

    @Query("SELECT v FROM ViajesEntity v LEFT JOIN FETCH v.pasajeros WHERE v.estado = :estado AND v.fechaHoraInicio <= :fechaHoraInicio")
    List<ViajesEntity> findByEstadoAndFechaHoraInicioLessThanEqualFetchPasajeros(@Param("estado") StatusEntity estado, @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio);
    @Query("SELECT v FROM ViajesEntity v LEFT JOIN FETCH v.pasajeros WHERE v.estado = :estado AND v.fechaHoraInicio BETWEEN :desde AND :hasta")
    List<ViajesEntity> findByEstadoAndFechaHoraInicioBetweenFetchPasajeros(@Param("estado") StatusEntity estado, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
    List<ViajesEntity> findByEstadoAndLocalidadInicioNot(StatusEntity estado, LocalidadEntity localidadInicio);

    // Buscar todos los viajes por objeto UserEntity (chofer) y lista de estados
    // Ajuste el nombre de la propiedad 'chofer' en lugar de 'user'
    List<ViajesEntity> findAllByChoferAndEstado(UserEntity chofer, StatusEntity estado);

    List<ViajesEntity> findAllByChoferAndEstadoIn(UserEntity chofer, List<StatusEntity> estados);

    // Contar viajes finalizados como chofer
    Long countByChoferAndEstado(UserEntity chofer, StatusEntity estado);

    // Contar viajes pendientes como chofer
    Long countByChoferAndEstadoIn(UserEntity chofer, List<StatusEntity> estado);


}
