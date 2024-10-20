package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.viajes.IncidenteEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.IncidenteRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.IncidenteService;
@Service
public class IncidenteServiceImpl implements IncidenteService {
    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private ViajeRepository viajeRepository;
    @Override
    public IncidenteEntity registrarIncidente(Long idViaje, IncidenteEntity incidente) {
        // Buscar el viaje por ID
        ViajesEntity viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + idViaje));

        // Asignar el viaje al incidente
        incidente.setViaje(viaje);

        // Guardar el incidente
        return incidenteRepository.save(incidente);
    }
}
