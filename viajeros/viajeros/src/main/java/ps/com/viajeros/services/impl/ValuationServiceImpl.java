package ps.com.viajeros.services.impl;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.valuations.ValuationRequestDto;
import ps.com.viajeros.dtos.valuations.ValuationResponseDto;
import ps.com.viajeros.entities.ValuationEntity;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ValuationRepository;
import ps.com.viajeros.services.ValuationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValuationServiceImpl implements ValuationService {

    @Autowired
    private ValuationRepository valuationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ValuationResponseDto createValuation(ValuationRequestDto valuationRequestDto) {
        // Buscar al usuario calificado por su ID
        UserEntity user = userRepository.findById(valuationRequestDto.getIdUserValuated())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Buscar al usuario calificado por su ID
        UserEntity UserWhoValuated = userRepository.findById(valuationRequestDto.getIdUserWhoValuated())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Crear una nueva entidad Valuation
        ValuationEntity valuationEntity = new ValuationEntity();
        valuationEntity.setIdTrip(valuationRequestDto.getIdTrip());
        valuationEntity.setUserWhoValuated(UserWhoValuated);
        valuationEntity.setUserValuated(user);
        valuationEntity.setRating(valuationRequestDto.getRating());
        valuationEntity.setComments(valuationRequestDto.getComments());

        // Guardar la valoración en la base de datos
        ValuationEntity savedValuation = valuationRepository.save(valuationEntity);

        // Devolver un ValuationResponseDto
        return convertToResponseDto(savedValuation);
    }

    @Override
    public List<ValuationResponseDto> getValuationsByTrip(Long idTrip) {
        List<ValuationEntity> valuations = valuationRepository.findByIdTrip(idTrip);
        return valuations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfPassengerRatedDriver(Long tripId, Long passengerId) {
        return valuationRepository.existsByTripIdAndPassengerIdAndValuatedIsDriver(tripId, passengerId);
    }

    // Método privado para convertir ValuationEntity a ValuationResponseDto
    private ValuationResponseDto convertToResponseDto(ValuationEntity valuationEntity) {
        ValuationResponseDto responseDto = new ValuationResponseDto();
        responseDto.setIdValuation(valuationEntity.getIdValuation());
        responseDto.setIdTrip(valuationEntity.getIdTrip());
        responseDto.setUserValuated(valuationEntity.getUserValuated().getIdUser());
        responseDto.setUserWhoValuated(valuationEntity.getUserWhoValuated().getIdUser());
        responseDto.setRating(valuationEntity.getRating());
        responseDto.setComments(valuationEntity.getComments());
        return responseDto;
    }
}
