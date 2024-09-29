package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.car.CarResponseDto;
import ps.com.viajeros.dtos.car.NewCarRequestDto;

import java.util.List;

@Service
public interface VehicleService {
    List<CarResponseDto> getAllCars(Long id);

    CarResponseDto registerVehicle(NewCarRequestDto newCarRequestDto);

    CarResponseDto delete(Long id);
}
