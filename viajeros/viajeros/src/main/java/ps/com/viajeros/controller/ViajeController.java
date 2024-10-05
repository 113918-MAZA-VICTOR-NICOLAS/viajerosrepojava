package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.services.LocalidadService;
import ps.com.viajeros.services.ViajeService;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

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

    @PostMapping("/buscar")
    public List<SearchResultMatchDto> buscarViajes(@RequestBody ViajesRequestMatchDto request) {
        return viajeService.findViajesByCriteria(request);
    }


    // Endpoint para listar viajes del mismo origen (sin importar el destino)
    @GetMapping("/origen/{origin}")
    public List<SearchResultMatchDto> buscarViajesPorOrigen(@PathVariable String origin) {
        return viajeService.findViajesByOrigin(origin);
    }

    // Endpoint para listar todos los viajes restantes
    @GetMapping("/todos")
    public List<SearchResultMatchDto> obtenerTodosLosViajesRestantes() {
        return viajeService.findAllViajes();
    }
}

