package ps.com.viajeros.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;

@Service
public interface ViajeService {

    void registerNewTrip(NewRequestViajeDto newTripRequestDto);
    List<SearchResultMatchDto> findViajesByCriteria(ViajesRequestMatchDto request);
}
