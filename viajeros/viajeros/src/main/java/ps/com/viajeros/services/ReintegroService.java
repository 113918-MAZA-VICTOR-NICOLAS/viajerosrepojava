package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.reintegro.ReintegroDto;
import ps.com.viajeros.dtos.reintegro.UpdateReintegroDto;

import java.util.List;

@Service
public interface ReintegroService {

    public ReintegroDto getReintegroDto(Long idReintegro);
    List<ReintegroDto> getAllReintegros();

    boolean updateReintegro(UpdateReintegroDto updateDto);
}
