package ps.com.viajeros.services.impl;

import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.ChoferEntity;
import ps.com.viajeros.services.ChoferService;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChoferServiceImpl implements ChoferService {
    @Override
    public List<ChoferEntity> getAllChofer() {
        List<ChoferEntity> choferes = new ArrayList<>();

        // Ejemplo de choferes
        ChoferEntity chofer1 = new ChoferEntity();
        chofer1.setIdChofer(1L);
        chofer1.setNameChofer("Juan Perez");

        ChoferEntity chofer2 = new ChoferEntity();
        chofer2.setIdChofer(2L);
        chofer2.setNameChofer("Maria Gonzalez");

        ChoferEntity chofer3 = new ChoferEntity();
        chofer3.setIdChofer(3L);
        chofer3.setNameChofer("Carlos Lopez");

        // Añadimos los choferes a la lista
        choferes.add(chofer1);
        choferes.add(chofer2);
        choferes.add(chofer3);

        return choferes;
    }

}
