package ps.com.viajeros.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.viaje.NewRequestViajeDto;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.ViajeService;

@Service
public class ViajeServiceImpl implements ViajeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ViajeRepository viajeRepository;

    public void registerNewTrip(NewRequestViajeDto newTripRequestDto) {
        // Aquí puedes implementar la lógica de negocio para convertir el DTO a una entidad y guardarlo en la base de datos
        ViajesEntity viajeEntity = new ViajesEntity();
        // Llenar la entidad con los datos del DTO
        viajeEntity = modelMapper.map(newTripRequestDto, ViajesEntity.class);

        // Lógica adicional para convertir los IDs en entidades, como vehículo, localidades, etc.

        // Guardar el viaje en la base de datos
        viajeRepository.save(viajeEntity);
    }
}
