package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;

import java.util.List;

@Repository
public interface LocalidadRepository extends JpaRepository<LocalidadEntity, Long> {

    @Query("SELECT l FROM LocalidadEntity l WHERE " +
            "LOWER(FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', l.localidad, 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')) " +
            "LIKE LOWER(CONCAT('%', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', " +
            "FUNCTION('REPLACE', :nombre, 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u'), '%')) " +
            "ORDER BY CASE WHEN LOWER(l.localidad) = LOWER(:nombre) THEN 0 ELSE 1 END, LENGTH(l.localidad)")
    List<LocalidadEntity> buscarLocalidadesIgnorandoAcentos(@Param("nombre") String nombre);


    // Encuentra la localidad por nombre
    LocalidadEntity findByLocalidad(String localidad);
}

