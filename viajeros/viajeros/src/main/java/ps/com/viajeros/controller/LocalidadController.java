package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;
import ps.com.viajeros.services.LocalidadService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {

    @Autowired
    private LocalidadService localidadService;

    // Endpoint para buscar localidades basadas en un término

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchLocalidades(@RequestParam String query) {
        // Buscar las localidades usando el servicio
        List<Map<String, Object>> localidades = localidadService.buscarLocalidadesPorNombre(query);

        // Retornar las localidades encontradas en formato JSON
        return ResponseEntity.ok(localidades);
    }
    @GetMapping("/id")
    public ResponseEntity<Map<String, String>> searchLocalidadById(@RequestParam Long id) {
        String localidad = localidadService.getLocalidadById(id);
        Map<String, String> response = new HashMap<>();
        response.put("localidad", localidad);
        return ResponseEntity.ok(response);
    }


}
