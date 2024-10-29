package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.incidente.IncidentesForAdminDto;
import ps.com.viajeros.dtos.incidente.ResolveIncidenteDto;
import ps.com.viajeros.dtos.viaje.incidentes.IncidenteDto;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.viajes.IncidenteEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.IncidenteRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.IncidenteService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncidenteServiceImpl implements IncidenteService {
    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public IncidenteEntity registrarIncidente(Long idViaje, IncidenteEntity incidente) {
        // Lógica para guardar el incidente
        return incidenteRepository.save(incidente);
    }

    // Método para obtener un viaje por su ID
    @Override
    public ViajesEntity obtenerViajePorId(Long idViaje) {
        return viajeRepository.findById(idViaje)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
    }

    // Método para obtener un usuario por su ID
    @Override
    public UserEntity obtenerUsuarioPorId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    // Método para obtener todos los incidentes y convertirlos a DTO
    @Override
    public List<IncidenteDto> getAllIncidentes() {
        List<IncidenteEntity> incidentes = incidenteRepository.findAll();

        // Convertimos las entidades a DTO usando Stream
        return incidentes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidentesForAdminDto> getAllIncidentesForAdmin() {
        return incidenteRepository.findAll().stream()
                .map(this::convertToDtoForAdmin)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenteEntity resolverIncidente(Long idIncidente, ResolveIncidenteDto resolveDto) {
        Optional<IncidenteEntity> incidenteOpt = incidenteRepository.findById(idIncidente);
        if (!incidenteOpt.isPresent()) {
            return null;
        }
        IncidenteEntity incidente = incidenteOpt.get();
        incidente.setEstadoResolucion(resolveDto.getEstadoResolucion());
        incidente.setResolucion(resolveDto.getResolucion());
        incidente.setFechaResolucion(LocalDateTime.now());  // Suponiendo que se resuelve en este momento

        // Si se provee un id de usuario, podríamos actualizar algún campo relacionado, como 'resueltoPorId'
        return incidenteRepository.save(incidente);
    }
    private IncidentesForAdminDto convertToDtoForAdmin(IncidenteEntity incidente) {
        IncidentesForAdminDto dto = new IncidentesForAdminDto();
        dto.setIdIncidente(incidente.getIdIncidente());
        dto.setIdDenunciado(incidente.getDenunciado().getIdUser());
        dto.setNombreCompletoDenunciado(incidente.getDenunciado().getName() + " " + incidente.getDenunciado().getLastname());
        dto.setIdDenunciante(incidente.getReportadoPor().getIdUser());
        dto.setNombreCompletoDenunciante(incidente.getReportadoPor().getName() + " " + incidente.getReportadoPor().getLastname());
        dto.setComentarios(incidente.getDescripcion());
        dto.setEstado(incidente.getEstadoResolucion().toString());
        dto.setTipoIncidente(incidente.getTipoIncidente().toString());
        dto.setFechaIncidente(incidente.getFechaIncidente());
        dto.setIsPasajero(incidente.getIsPasajero());
        dto.setResolucion(incidente.getResolucion());
        dto.setFechaResolucion(incidente.getFechaResolucion());
        return dto;
    }

    // Método para convertir de IncidenteEntity a IncidenteDto
    private IncidenteDto convertToDto(IncidenteEntity incidenteEntity) {
        return IncidenteDto.builder()
                .idIncidente(incidenteEntity.getIdIncidente())
                .viajeId(incidenteEntity.getViaje().getIdViaje())
                .descripcion(incidenteEntity.getDescripcion())
                .tipoIncidente(incidenteEntity.getTipoIncidente())
                .fechaIncidente(incidenteEntity.getFechaIncidente())
                .denunciadoId(incidenteEntity.getDenunciado().getIdUser())  // ID del denunciado
                .reportadoPorId(incidenteEntity.getReportadoPor().getIdUser())  // ID del usuario que reportó
                .estadoResolucion(incidenteEntity.getEstadoResolucion())
                .resolucion(incidenteEntity.getResolucion())
                .fechaResolucion(incidenteEntity.getFechaResolucion())
                .isPasajero(incidenteEntity.getIsPasajero())  // Añadir isPasajero correctamente
                .build();
    }


}
