package ps.com.viajeros.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.ValuationEntity;
import ps.com.viajeros.entities.VehicleEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;
import ps.com.viajeros.repository.*;
import ps.com.viajeros.services.ViajeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
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
        List<ViajesEntity> viajes = viajeRepository.findByLocalidadInicioAndLocalidadFin(
                request.getOrigin(),
                request.getDestination()
        );

        return viajes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
                .build();
    }

    private double calculateAverageRating(UserEntity chofer) {
        List<ValuationEntity> valuations = chofer.getValuations(); // Asumiendo que tienes un getter para las valoraciones
        if (valuations.isEmpty()) {
            return 0; // Devuelve 0 si no hay valoraciones
        }

        double sum = valuations.stream()
                .mapToDouble(ValuationEntity::getRating)
                .sum();

        return sum / valuations.size();
    }
}
