package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.ChoferEntity;

import java.util.List;
@Service
public interface ChoferService {

    List<ChoferEntity> getAllChofer();
}
