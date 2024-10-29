package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.reintegro.ReintegroDto;
import ps.com.viajeros.dtos.reintegro.UpdateReintegroDto;
import ps.com.viajeros.services.ReintegroService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reintegros")
public class ReintegroController {
    @Autowired
    private ReintegroService reintegroService;
    @GetMapping("/{id}")
    public ResponseEntity<ReintegroDto> getReintegro(@PathVariable Long id) {
        try {
            ReintegroDto reintegroDto = reintegroService.getReintegroDto(id);
            return ResponseEntity.ok(reintegroDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReintegroDto>> getAllReintegros() {
        List<ReintegroDto> reintegros = reintegroService.getAllReintegros();
        return ResponseEntity.ok(reintegros);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateReintegro(@RequestBody UpdateReintegroDto updateDto) {
        boolean updated = reintegroService.updateReintegro(updateDto);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Reintegro actualizado correctamente"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al actualizar el reintegro"));
        }
    }

}
