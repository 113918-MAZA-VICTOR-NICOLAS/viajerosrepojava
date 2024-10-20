package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.viajes.IncidenteEntity;

@Service
public interface IncidenteService {
    IncidenteEntity registrarIncidente(Long idViaje, IncidenteEntity incidente);
}
