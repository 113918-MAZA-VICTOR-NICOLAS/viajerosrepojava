package ps.com.viajeros.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ps.com.viajeros.dtos.admin.ViajeDto;
import ps.com.viajeros.services.ViajeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ViajeService viajeService; // Servicio que maneja la lógica de los viajes

    public AdminController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    // Endpoint para obtener todos los viajes
    @GetMapping("/viajes")
    public ResponseEntity<List<ViajeDto>> getAllViajes(
            @RequestParam(value = "status", required = false) String status) {

        List<ViajeDto> viajes;

        if (status != null) {
            // Filtra por estado si se proporciona
            viajes = viajeService.getViajesByStatus(status);
        } else {
            // Si no se proporciona estado, devuelve todos los viajes
            viajes = viajeService.getAllViajes();
        }

        return ResponseEntity.ok(viajes);
    }

    // Nuevo endpoint para obtener viajes filtrados por rango de fechas
    @GetMapping("/viajes/fecha")
    public ResponseEntity<List<ViajeDto>> getViajesPorFecha(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Convertir `LocalDate` a `LocalDateTime` al inicio y final del día
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<ViajeDto> viajes = viajeService.getViajesByFecha(startDateTime, endDateTime);
        return ResponseEntity.ok(viajes);
    }
}
