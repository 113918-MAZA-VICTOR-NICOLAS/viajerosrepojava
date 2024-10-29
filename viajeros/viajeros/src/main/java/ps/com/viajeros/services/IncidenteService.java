package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.incidente.IncidentesForAdminDto;
import ps.com.viajeros.dtos.incidente.ResolveIncidenteDto;
import ps.com.viajeros.dtos.viaje.incidentes.IncidenteDto;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.viajes.IncidenteEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;

@Service
public interface IncidenteService {
    IncidenteEntity registrarIncidente(Long idViaje, IncidenteEntity incidente);

    UserEntity obtenerUsuarioPorId(Long userId);
    ViajesEntity obtenerViajePorId(Long idViaje);

    List<IncidenteDto> getAllIncidentes();

    List<IncidentesForAdminDto> getAllIncidentesForAdmin();

    IncidenteEntity resolverIncidente(Long idIncidente, ResolveIncidenteDto resolveDto);
}
