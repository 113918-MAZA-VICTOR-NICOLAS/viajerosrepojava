package ps.com.viajeros.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.admin.ViajeDto;
import ps.com.viajeros.dtos.statistic.EstadoViajesDto;
import ps.com.viajeros.dtos.statistic.ViajesPorMesDto;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.ValuationEntity;
import ps.com.viajeros.entities.VehicleEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;
import ps.com.viajeros.repository.*;
import ps.com.viajeros.services.ViajeService;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ViajeServiceImpl implements ViajeService {

    private static final Logger logger = LoggerFactory.getLogger(ViajeService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalidadRepository localidadRepository;
    @Autowired
    private StatusViajeRepository statusViajeRepository;

    @Override

    public void registerNewTrip(NewRequestViajeDto newTripRequestDto) {
        try {
            // Crear una nueva instancia de ViajesEntity
            ViajesEntity viajeEntity = new ViajesEntity();

            // Obtener el vehículo por su ID
            VehicleEntity vehicle = vehicleRepository.findById(newTripRequestDto.getIdVehiculo())
                    .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

            // Obtener el chofer por su ID
            UserEntity chofer = userRepository.findById(newTripRequestDto.getIdChofer())
                    .orElseThrow(() -> new IllegalArgumentException("Chofer no encontrado"));

            // Obtener la localidad de inicio por su ID
            LocalidadEntity localidadInicio = localidadRepository.findById(newTripRequestDto.getLocalidadInicioId())
                    .orElseThrow(() -> new IllegalArgumentException("Localidad de inicio no encontrada"));

            // Obtener la localidad de fin por su ID
            LocalidadEntity localidadFin = localidadRepository.findById(newTripRequestDto.getLocalidadFinId())
                    .orElseThrow(() -> new IllegalArgumentException("Localidad de fin no encontrada"));


            StatusEntity status = statusViajeRepository.getById(2L);
            // Asignar los valores a la entidad de viaje
            viajeEntity.setVehiculo(vehicle);
            viajeEntity.setChofer(chofer);
            viajeEntity.setEstado(status);
            viajeEntity.setLocalidadInicio(localidadInicio);
            viajeEntity.setLocalidadFin(localidadFin);
            viajeEntity.setFechaHoraInicio(newTripRequestDto.getFechaHoraInicio());
            viajeEntity.setGastoTotal(newTripRequestDto.getGastoTotal());
            viajeEntity.setAsientosDisponibles(newTripRequestDto.getAsientosDisponibles());
            viajeEntity.setMascotas(newTripRequestDto.isAceptaMascotas());
            viajeEntity.setFumar(newTripRequestDto.isAceptaFumar());

            // Guardar la entidad de viaje en la base de datos
            viajeRepository.save(viajeEntity);

        } catch (Exception e) {
            // Registrar la excepción en los logs
            logger.error("Error al registrar el viaje: ", e);

            // Lanza una excepción personalizada o arroja el error para devolver al cliente
            throw new RuntimeException("Error al registrar el viaje", e);
        }
    }

    @Override
    public List<SearchResultMatchDto> findViajesByCriteria(ViajesRequestMatchDto request) {
        // Consultar las entidades LocalidadEntity para origen y destino usando los ids
        LocalidadEntity originEntity = localidadRepository.findById(request.getLocalidadInicioId())
                .orElseThrow(() -> new IllegalArgumentException("Origen no encontrado"));
        LocalidadEntity destinationEntity = localidadRepository.findById(request.getLocalidadFinId())
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));

        // Buscar la entidad StatusEntity con id_status 2
        StatusEntity status = statusViajeRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        // Pasar las entidades completas a la consulta del repositorio de viajes filtrando por estado
        List<ViajesEntity> viajes = viajeRepository.findByLocalidadInicioAndLocalidadFinAndEstado(originEntity, destinationEntity, status);

        return viajes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<SearchResultMatchDto> findViajesByOrigin(ViajesRequestMatchDto request) {
        // Verificar si el idlocalidadInicioId fue proporcionado en la request
        if (request.getLocalidadInicioId() == null) {
            // Si no se proporciona el id de la localidad de inicio, devolver una lista vacía
            return List.of();
        }

        // Buscar la entidad LocalidadEntity basada en el idlocalidadInicioId
        LocalidadEntity localidadInicio = localidadRepository.findById(request.getLocalidadInicioId())
                .orElseThrow(() -> new IllegalArgumentException("Localidad de origen no encontrada"));

        // Buscar la entidad StatusEntity con id_status 2
        StatusEntity status = statusViajeRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        // Usar la entidad LocalidadEntity para buscar los viajes y filtrar por el estado
        List<ViajesEntity> viajes = viajeRepository.findByLocalidadInicioAndEstado(localidadInicio, status);

        // Convertir los viajes encontrados a DTO y devolver la lista
        return viajes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }




    @Override
    public List<SearchResultMatchDto> findAllViajesCreated(ViajesRequestMatchDto request) {
        // Buscar la entidad StatusEntity con id_status 2
        StatusEntity status = statusViajeRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        // Filtrar los viajes por el estado encontrado (id_status 2)
        List<ViajesEntity> viajes = viajeRepository.findByEstado(status);

        // Convertir los viajes filtrados a DTO y devolver la lista
        return viajes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SearchResultMatchDto> findAllViajesCreatedExeptOrigin(ViajesRequestMatchDto request) {
        // Verificar si se proporciona el id de la localidad de origen
        if (request.getLocalidadInicioId() == null) {
            throw new IllegalArgumentException("El ID de la localidad de origen es requerido");
        }

        // Buscar la entidad StatusEntity con id_status 2
        StatusEntity status = statusViajeRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        // Buscar la entidad LocalidadEntity basada en el idlocalidadInicioId
        LocalidadEntity localidadInicio = localidadRepository.findById(request.getLocalidadInicioId())
                .orElseThrow(() -> new IllegalArgumentException("Localidad de origen no encontrada"));

        // Filtrar los viajes por estado, excluyendo los que tienen la localidad de origen dada
        List<ViajesEntity> viajes = viajeRepository.findByEstadoAndLocalidadInicioNot(status, localidadInicio);

        // Convertir los viajes filtrados a DTO y devolver la lista
        return viajes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SearchResultMatchDto> findAllCreatedAndInProgressByUser(Long userId) {
        // Buscar el usuario por su ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar los estados 2 (CREATED) y 3 (INPROGRESS)
        List<StatusEntity> estados = statusViajeRepository.findAllById(List.of(2L, 3L));

        if (estados.isEmpty()) {
            throw new IllegalArgumentException("Estados no encontrados");
        }

        // Buscar los viajes donde el usuario es chofer o pasajero, y los estados coinciden
        List<ViajesEntity> viajesComoChofer = viajeRepository.findAllByChoferAndEstadoIn(user, estados);
        List<ViajesEntity> viajesComoPasajero = viajeRepository.findAllByPasajerosContainingAndEstadoIn(user, estados);

        // Combinar ambas listas y eliminar duplicados (en caso de que el usuario sea chofer y pasajero a la vez)
        Set<ViajesEntity> viajes = new HashSet<>();
        viajes.addAll(viajesComoChofer);
        viajes.addAll(viajesComoPasajero);

        // Reutilizar tu método convertToDto
        return viajes.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    @Override
    public List<SearchResultMatchDto> findAllFinishedByUser(Long userId) {
        // Buscar el usuario por su ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar el estado 6 (FINISHED)
        StatusEntity estado = statusViajeRepository.findById(6L)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));

        // Buscar los viajes donde el usuario es chofer y están finalizados
        List<ViajesEntity> viajesComoChofer = viajeRepository.findAllByChoferAndEstado(user, estado);

        // Buscar los viajes donde el usuario es pasajero y están finalizados
        List<ViajesEntity> viajesComoPasajero = viajeRepository.findAllByPasajeroAndEstado(user, estado);

        // Combinar ambas listas de viajes (eliminando duplicados si los hay)
        Set<ViajesEntity> todosLosViajes = new HashSet<>(viajesComoChofer);
        todosLosViajes.addAll(viajesComoPasajero);

        // Convertir la lista a DTOs y devolver
        return todosLosViajes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteViajeLogicamente(Long viajeId) {
        // Buscar el viaje por su ID
        ViajesEntity viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        // Verificar si el viaje tiene pasajeros
        if (viaje.getPasajeros() != null && !viaje.getPasajeros().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el viaje porque ya tiene pasajeros");
        }

        // Buscar el estado 'deleted' (supongamos que su ID es 5)
        StatusEntity statusDeleted = statusViajeRepository.findById(5L)
                .orElseThrow(() -> new IllegalArgumentException("Estado 'deleted' no encontrado"));

        // Cambiar el estado del viaje a 'deleted'
        viaje.setEstado(statusDeleted);

        // Guardar los cambios
        viajeRepository.save(viaje);
    }


    private SearchResultMatchDto convertToDto(ViajesEntity entity) {
        return SearchResultMatchDto.builder()
                .tripId(entity.getIdViaje())
                .origin(entity.getLocalidadInicio().getLocalidad())
                .destination(entity.getLocalidadFin().getLocalidad())
                .availableSeats(entity.getAsientosDisponibles())
                .date(entity.getFechaHoraInicio())
                .departureTime(entity.getFechaHoraInicio())
                .arrivalTime(entity.getFechaHoraFin())
                .estimatedDuration("Estimar duración")
                .petsAllowed(entity.isMascotas())
                .smokersAllowed(entity.isFumar())
                .vehicleName(entity.getVehiculo().getBrand()+" " + entity.getVehiculo().getModel())
                .driverRating(calculateAverageRating(entity.getChofer()))
                .driverName(entity.getChofer().getName())
                .driverId(entity.getChofer().getIdUser())
                .status(entity.getEstado().getName())
                .build();
    }

    private double calculateAverageRating(UserEntity chofer) {
        List<ValuationEntity> valuations = chofer.getReceivedValuations(); // Asumiendo que tienes un getter para las valoraciones
        if (valuations.isEmpty()) {
            return 0; // Devuelve 0 si no hay valoraciones
        }

        double sum = valuations.stream()
                .mapToDouble(ValuationEntity::getRating)
                .sum();

        return sum / valuations.size();
    }

    @Override
    public List<SearchResultMatchDto> findAllCreated() {
        StatusEntity statusCreated = statusViajeRepository.findById(2L) // Buscamos el estado "Created" con id 2
                .orElseThrow(() -> new IllegalArgumentException("Estado 'Created' no encontrado"));
        List<ViajesEntity> viajes = viajeRepository.findByEstado(statusCreated);

        // Mapear a DTOs
        return viajes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Long getChoferByTrip(Long idTrip) {
        ViajesEntity viaje = viajeRepository.findById(idTrip).orElse(null); // Usamos findById para manejar opcional
        if (viaje != null && viaje.getChofer() != null) {
            return viaje.getChofer().getIdUser(); // Obtenemos el id del chofer
        }
        return null; // Retorna null si no hay viaje o no hay chofer asociado
    }

    @Override
    public SearchResultMatchDto convertToDtoById(ViajesEntity viaje) {
        return convertToDto(viaje);
    }


    // Obtener todos los viajes
    public List<ViajeDto> getAllViajes() {
        List<ViajesEntity> viajes = viajeRepository.findAll();  // Obtiene todos los viajes
        return viajes.stream().map(this::convertToViajesDto).toList();  // Convierte las entidades a DTO
    }

    // Filtrar viajes por estado
    public List<ViajeDto> getViajesByStatus(String statusName) {
        // Busca la entidad StatusEntity por nombre de estado
        StatusEntity status = statusViajeRepository.findByName(statusName)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + statusName));

        // Filtra los viajes que tengan este estado
        List<ViajesEntity> viajes = viajeRepository.findByEstado(status);

        return viajes.stream().map(this::convertToViajesDto).toList();  // Convierte las entidades a DTO
    }
    // Método auxiliar para convertir ViajesEntity a ViajeDto
    private ViajeDto convertToViajesDto(ViajesEntity viaje) {
        return new ViajeDto(
                viaje.getIdViaje(),
                viaje.getChofer().getName() + " " + viaje.getChofer().getLastname(),  // Nombre del chofer
                viaje.getPasajeros().stream().map(p -> p.getName() + " " + p.getLastname()).toList(),  // Lista de nombres de pasajeros
                viaje.getLocalidadInicio().getLocalidad(),  // Origen
                viaje.getLocalidadFin().getLocalidad(),  // Destino
                viaje.getFechaHoraInicio(),  // Fecha de inicio
                viaje.getEstado().getName()  // Estado
        );
    }
    @Override
    public List<ViajesPorMesDto> getViajesFinalizadosPorMes() {
        // Lógica para obtener viajes finalizados por mes
        List<ViajesPorMesDto> viajesFinalizados = viajeRepository.getViajesFinalizadosPorMes();
        return viajesFinalizados;
    }
    @Override
    public List<EstadoViajesDto> getEstadoDeLosViajes() {
        return viajeRepository.getEstadoDeLosViajes();
    }

    @Override
    public ViajesEntity getTripById(Long tripId) {
        return viajeRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
    }


    @Override
    public ViajesEntity updateTrip(Long id, NewRequestViajeDto tripDto) {
        ViajesEntity existingTrip = viajeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id: " + id));

        existingTrip.setVehiculo(vehicleRepository.findById(tripDto.getIdVehiculo())
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con id: " + tripDto.getIdVehiculo())));

        existingTrip.setLocalidadInicio(localidadRepository.findById(tripDto.getLocalidadInicioId())
                .orElseThrow(() -> new EntityNotFoundException("Localidad de inicio no encontrada")));

        existingTrip.setLocalidadFin(localidadRepository.findById(tripDto.getLocalidadFinId())
                .orElseThrow(() -> new EntityNotFoundException("Localidad de fin no encontrada")));

        existingTrip.setFechaHoraInicio(tripDto.getFechaHoraInicio());
        existingTrip.setGastoTotal(tripDto.getGastoTotal());
        existingTrip.setAsientosDisponibles(tripDto.getAsientosDisponibles());
        existingTrip.setMascotas(tripDto.isAceptaMascotas());
        existingTrip.setFumar(tripDto.isAceptaFumar());

        // Actualizamos el viaje
        ViajesEntity updatedTrip = viajeRepository.save(existingTrip);

        return updatedTrip;
    }



}
