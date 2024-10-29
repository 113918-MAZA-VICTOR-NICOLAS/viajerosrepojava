package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.valuations.ValuationRequestDto;
import ps.com.viajeros.dtos.valuations.ValuationResponseDto;
import ps.com.viajeros.services.ValuationService;

import java.util.List;

@RestController
@RequestMapping("/api/valuations")
public class ValuationsController {

    @Autowired
    private ValuationService valuationService;

    // Endpoint para crear una valoración
    @PostMapping("/submit")
    public ResponseEntity<ValuationResponseDto> submitValuation(@RequestBody ValuationRequestDto valuationRequestDto) {
        ValuationResponseDto responseDto = valuationService.createValuation(valuationRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Endpoint para obtener todas las valoraciones de un viaje
    @GetMapping("/trip/{idTrip}")
    public ResponseEntity<List<ValuationResponseDto>> getValuationsByTrip(@PathVariable Long idTrip) {
        List<ValuationResponseDto> valuations = valuationService.getValuationsByTrip(idTrip);
        return ResponseEntity.ok(valuations);
    }

    @GetMapping("/has-rated")
    public ResponseEntity<Boolean> hasPassengerBeenRated(@RequestParam("idTrip") Long idTrip, @RequestParam("idPassenger") Long idPassenger) {
        boolean hasRated = valuationService.checkIfPassengerRatedDriver(idTrip, idPassenger);
        return ResponseEntity.ok(hasRated);
    }
}
