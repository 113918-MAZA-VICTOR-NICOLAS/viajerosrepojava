package ps.com.viajeros.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.admin.ViajeDto;
import ps.com.viajeros.dtos.statistic.EstadoViajesDto;
import ps.com.viajeros.dtos.statistic.ViajesPorMesDto;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.dtos.viaje.SearchResultMatchDto;
import ps.com.viajeros.dtos.viaje.ViajesRequestMatchDto;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;

@Service
public interface ViajeService {

    void registerNewTrip(NewRequestViajeDto newTripRequestDto);
    List<SearchResultMatchDto> findViajesByCriteria(ViajesRequestMatchDto request);
    List<SearchResultMatchDto> findViajesByOrigin(ViajesRequestMatchDto request);
    List<SearchResultMatchDto> findAllViajesCreated(ViajesRequestMatchDto request);

    List<SearchResultMatchDto> findAllViajesCreatedExeptOrigin(ViajesRequestMatchDto request);

    List<SearchResultMatchDto> findAllCreatedAndInProgressByUser(Long userId);

    List<SearchResultMatchDto> findAllFinishedByUser(Long userId);

    void deleteViajeLogicamente(Long viajeId);

    List<SearchResultMatchDto> findAllCreated();

    Long getChoferByTrip(Long idTrip);


    SearchResultMatchDto convertToDtoById(ViajesEntity viaje);


    List<ViajeDto> getAllViajes();

    List<ViajeDto> getViajesByStatus(String statusName);

    List<ViajesPorMesDto> getViajesFinalizadosPorMes();

    List<EstadoViajesDto> getEstadoDeLosViajes();

    ViajesEntity getTripById(Long tripId);

    ViajesEntity updateTrip(Long id, NewRequestViajeDto tripDto);
}
