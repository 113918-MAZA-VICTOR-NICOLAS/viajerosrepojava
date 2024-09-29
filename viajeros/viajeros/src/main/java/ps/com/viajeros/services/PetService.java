package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.pet.NewPetRequestDto;
import ps.com.viajeros.dtos.pet.PetResponseDto;

import java.util.List;

@Service
public interface PetService {
    List<PetResponseDto> getAllPetsFromUser(Long idUser);

    PetResponseDto createNewPet(NewPetRequestDto newPetRequestDto);

    PetResponseDto updatePet(Long petId, PetResponseDto updatedPetRequest);

    PetResponseDto deletePet(Long petId);
}
