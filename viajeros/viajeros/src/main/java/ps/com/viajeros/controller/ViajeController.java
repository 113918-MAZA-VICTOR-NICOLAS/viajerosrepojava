package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.chat.IsChoferDto;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.PassengersDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.StatusViajeRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.LocalidadService;
import ps.com.viajeros.services.PaymentService;
import ps.com.viajeros.services.ViajeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StatusViajeRepository statusViajeRepository;

    // Endpoint para registrar un nuevo viaje
    @PostMapping("/register")
    public ResponseEntity<String> registerViaje(@RequestBody NewRequestViajeDto newTripRequestDto) {
        try {
            // Llamada al servicio para registrar el viaje
            viajeService.registerNewTrip(newTripRequestDto);
            return ResponseEntity.ok("Viaje registrado exitosamente");
        } catch (Exception e) {
            // Manejar errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el viaje");
        }
    }

    // Endpoint para buscar viajes por origen y destino
    @PostMapping("/buscarorigenydestino")
    public List<SearchResultMatchDto> buscarViajesOrigenYdestino(@RequestBody ViajesRequestMatchDto request) {
        return viajeService.findViajesByCriteria(request);
    }

    // Endpoint para listar viajes del mismo origen (sin importar el destino)
    @PostMapping("/origen")
    public List<SearchResultMatchDto> buscarViajesPorOrigen(@RequestBody ViajesRequestMatchDto request) {
        return viajeService.findViajesByOrigin(request);
    }

    // Endpoint para listar todos los viajes restantes excepto el origen
    @PostMapping("/todosCreated")
    public List<SearchResultMatchDto> obtenerTodosLosViajesExceptOrigin(@RequestBody ViajesRequestMatchDto request) {
        return viajeService.findAllViajesCreatedExeptOrigin(request);
    }

    @GetMapping("/user/{userId}/created-inprogress")
    public ResponseEntity<List<SearchResultMatchDto>> getAllCreatedAndInProgressByUser(@PathVariable Long userId) {
        try {
            List<SearchResultMatchDto> viajes = viajeService.findAllCreatedAndInProgressByUser(userId);
            return ResponseEntity.ok(viajes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para obtener viajes con estado FINISHED (6) por id de usuario
    @GetMapping("/user/{userId}/finished")
    public ResponseEntity<List<SearchResultMatchDto>> getAllFinishedByUser(@PathVariable Long userId) {
        try {
            List<SearchResultMatchDto> viajes = viajeService.findAllFinishedByUser(userId);
            return ResponseEntity.ok(viajes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/delete/{viajeId}")
    public ResponseEntity<String> deleteViajeLogicamente(@PathVariable Long viajeId) {
        try {
            viajeService.deleteViajeLogicamente(viajeId);
            return ResponseEntity.ok("Viaje eliminado lógicamente exitosamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el viaje");
        }
    }

    @GetMapping("/trips/created")
    public ResponseEntity<List<SearchResultMatchDto>> getAllCreatedTrips() {
        try {
            List<SearchResultMatchDto> viajes = viajeService.findAllCreated();
            return ResponseEntity.ok(viajes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/getChofer")
    public ResponseEntity<Long> choferByTrip(@RequestParam Long idTrip) {
        try {
            Long idChofer = viajeService.getChoferByTrip(idTrip); // Asume que existe un método en viajeService para obtener el id del chofer
            return ResponseEntity.ok(idChofer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/isChofer/{tripId}/{userId}")
    public ResponseEntity<IsChoferDto> soyChofer(@PathVariable Long tripId, @PathVariable Long userId) {
        // Buscar el viaje por el ID
        ViajesEntity viaje = viajeRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Obtener el chofer del viaje
        UserEntity chofer = viaje.getChofer();

        // Comprobar si el usuario es el chofer
        boolean esChofer = chofer.getIdUser().equals(userId);

        // Obtener el nombre del chofer
        String nombreChofer = chofer.getName();

        // Obtener los datos del pasajero si no es el chofer
        String nombrePasajero = esChofer ? null : userRepository.findById(userId)
                .map(UserEntity::getName)
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

        // Crear el DTO de respuesta
        IsChoferDto isChoferDto = IsChoferDto.builder()
                .ischofer(esChofer)
                .idChofer(chofer.getIdUser())
                .nombreChofer(nombreChofer)
                .idPasajero(esChofer ? null : userId)
                .nombrePasajero(nombrePasajero)
                .build();

        // Devolver la respuesta
        return ResponseEntity.ok(isChoferDto);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<SearchResultMatchDto> getTripById(@PathVariable Long tripId) {
        try {
            // Buscar el viaje por ID
            ViajesEntity viaje = viajeRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

            // Convertir el viaje a SearchResultMatchDto
            SearchResultMatchDto tripDto = viajeService.convertToDtoById(viaje);

            // Devolver la respuesta
            return ResponseEntity.ok(tripDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/passengers/{tripId}")
    public ResponseEntity<List<PassengersDto>> getPassengersByTripId(@PathVariable Long tripId) {
        try {
            // Buscar el viaje por ID
            ViajesEntity viaje = viajeRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

            // Obtener la lista de pasajeros del viaje
            List<UserEntity> passengers = viaje.getPasajeros();

            // Convertir la lista de UserEntity a una lista de PassengersDto
            List<PassengersDto> passengerDtos = passengers.stream()
                    .map(pasajero -> new PassengersDto(pasajero.getIdUser(), pasajero.getName(), pasajero.getLastname()))
                    .collect(Collectors.toList());

            // Devolver la respuesta con la lista de pasajeros
            return ResponseEntity.ok(passengerDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{tripId}/remove-passenger/{userId}")
    public ResponseEntity<String> removePassengerFromTrip(@PathVariable Long tripId, @PathVariable Long userId) {
        try {
            // Buscar el viaje y el pasajero
            ViajesEntity viaje = viajeRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
            UserEntity pasajero = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

            // Eliminar al pasajero del viaje
            viaje.getPasajeros().remove(pasajero);
            viajeRepository.save(viaje);

            // Solicitar reintegro
            paymentService.requestReintegroByPassenger(tripId, userId);

            return ResponseEntity.ok("Pasajero eliminado y reintegro solicitado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar al pasajero y solicitar reintegro");
        }
    }
    @PostMapping("/{tripId}/finalizar")
    public ResponseEntity<String> finalizarViaje(@PathVariable Long tripId) {
        ViajesEntity viaje = viajeRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        if (!viaje.getEstado().getIdState().equals(3L)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El viaje no está en progreso");
        }
        StatusEntity status = statusViajeRepository.getReferenceById(6L);
        viaje.setEstado(status);
        viajeRepository.save(viaje);

        return ResponseEntity.ok("Viaje finalizado con éxito");
    }

}

