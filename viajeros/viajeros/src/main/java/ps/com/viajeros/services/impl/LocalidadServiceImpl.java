package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;
import ps.com.viajeros.repository.LocalidadRepository;
import ps.com.viajeros.services.LocalidadService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocalidadServiceImpl implements LocalidadService {
    @Autowired
    private LocalidadRepository localidadRepository;

    @Override
    public List<Map<String, Object>> buscarLocalidadesPorNombre(String nombre) {
        nombre = nombre.toLowerCase();

        List<LocalidadEntity> localidades = localidadRepository.buscarLocalidadesIgnorandoAcentos(nombre);

        return localidades.stream().map(localidad -> {
            Map<String, Object> localidadMap = new HashMap<>();
            localidadMap.put("id", localidad.getId());
            localidadMap.put("nombre", localidad.getLocalidad() + " (" + localidad.getProvincia().getProvincia() + ")"); // Añadir la provincia
            return localidadMap;
        }).collect(Collectors.toList());
    }


}
