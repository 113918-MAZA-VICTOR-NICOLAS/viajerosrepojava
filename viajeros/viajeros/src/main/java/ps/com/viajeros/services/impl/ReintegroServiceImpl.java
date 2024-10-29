package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.reintegro.ReintegroDto;
import ps.com.viajeros.dtos.reintegro.UpdateReintegroDto;
import ps.com.viajeros.entities.payment.ReintegroEntity;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.repository.ReintegroRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.services.ReintegroService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReintegroServiceImpl implements ReintegroService {


    @Autowired
    private ReintegroRepository reintegroRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ReintegroDto getReintegroDto(Long idReintegro) {
        ReintegroEntity reintegro = reintegroRepository.findById(idReintegro).orElseThrow(() -> new RuntimeException("Reintegro not found"));
        return convertToDto(reintegro);
    }

    @Override
    public List<ReintegroDto> getAllReintegros() {
        List<ReintegroEntity> reintegros = reintegroRepository.findAll();
        return reintegros.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public boolean updateReintegro(UpdateReintegroDto updateDto) {
        // Aquí deberías implementar la lógica para buscar y actualizar el reintegro en la base de datos
        ReintegroEntity reintegro = reintegroRepository.findById(updateDto.getIdReintegro()).orElse(null);
        if (reintegro == null) {
            return false;
        }
        reintegro.setStatus(updateDto.getNuevoEstado());
        // Aquí se podrían agregar más campos si es necesario

        UserEntity user = userRepository.findById(updateDto.getIdAdministrador()).get();
        reintegro.setAdminReintegro(user);
        reintegroRepository.save(reintegro);
        return true;
    }
    private ReintegroDto convertToDto(ReintegroEntity reintegro) {
        ReintegroDto dto = new ReintegroDto();
        dto.setIdReintegro(reintegro.getIdReintegro());
        dto.setStatus(reintegro.getStatus().toString());
        dto.setAdminReintegroId(reintegro.getAdminReintegro() != null ? reintegro.getAdminReintegro().getIdUser() : null);
        dto.setFechaReintegro(reintegro.getFechaReintegro());
        dto.setReintegroMotivo(reintegro.getReintegroMotivo().toString());
        if (reintegro.getAdminReintegro() != null) {
            dto.setNombreAdminReintegro(reintegro.getAdminReintegro().getName() + " " + reintegro.getAdminReintegro().getLastname());

        }
        dto.setPaymentId(reintegro.getPayment().getIdPayment());
        return dto;
    }
}
