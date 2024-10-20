package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.entities.viajes.IncidenteEntity;
import ps.com.viajeros.services.IncidenteService;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    @PostMapping("/registrar/{idViaje}")
    public ResponseEntity<IncidenteEntity> registrarIncidente(@PathVariable Long idViaje, @RequestBody IncidenteEntity incidente) {
        IncidenteEntity nuevoIncidente = incidenteService.registrarIncidente(idViaje, incidente);
        return new ResponseEntity<>(nuevoIncidente, HttpStatus.CREATED);
    }
}
