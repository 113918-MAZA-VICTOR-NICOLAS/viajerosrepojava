package ps.com.viajeros.services;

import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;

import java.util.List;
import java.util.Map;

public interface LocalidadService {
    List<Map<String, Object>> buscarLocalidadesPorNombre(String nombre);
}
