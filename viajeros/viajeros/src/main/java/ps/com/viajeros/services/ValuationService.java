package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.valuations.ValuationRequestDto;
import ps.com.viajeros.dtos.valuations.ValuationResponseDto;

import java.util.List;
@Service
public interface ValuationService {

    ValuationResponseDto createValuation(ValuationRequestDto valuationRequestDto);

    List<ValuationResponseDto> getValuationsByTrip(Long idTrip);

    boolean checkIfPassengerRatedDriver(Long idTrip, Long idPassenger);
}
