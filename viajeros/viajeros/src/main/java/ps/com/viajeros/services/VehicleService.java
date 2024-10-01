package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.car.CarResponseDto;
import ps.com.viajeros.dtos.car.NewCarRequestDto;

import java.util.List;
import java.util.Optional;

@Service
public interface VehicleService {
    List<CarResponseDto> getAllCars(Long id);

    CarResponseDto registerVehicle(NewCarRequestDto newCarRequestDto);

    CarResponseDto delete(Long id);

  CarResponseDto updateVehicle(Long id, CarResponseDto updatedVehicle);
}
