package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.car.CarResponseDto;
import ps.com.viajeros.dtos.car.NewCarRequestDto;
import ps.com.viajeros.dtos.common.ErrorApi;
import ps.com.viajeros.entities.ChoferEntity;
import ps.com.viajeros.services.ChoferService;
import ps.com.viajeros.services.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/userVehicles/{id}")
    public ResponseEntity<Object> getAllVehiclesFromUser(@PathVariable Long id) {
        try {
            List<CarResponseDto> vehicles = vehicleService.getAllCars(id);
            return ResponseEntity.ok(vehicles);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorApi(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error obtaining vehicles", ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerVehicle(@RequestBody NewCarRequestDto requestDto) {
        try {
            CarResponseDto carloaded = vehicleService.registerVehicle(requestDto);
            return ResponseEntity.ok(carloaded);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorApi(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error registering vehicle", ex.getMessage()));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable Long id) {
        try {
            CarResponseDto cardeleted = vehicleService.delete(id);
            return ResponseEntity.ok(cardeleted);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorApi(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error registering vehicle", ex.getMessage()));
        }
    }

}
