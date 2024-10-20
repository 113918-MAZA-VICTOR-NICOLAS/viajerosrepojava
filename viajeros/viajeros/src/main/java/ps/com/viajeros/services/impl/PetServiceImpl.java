package ps.com.viajeros.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.pet.NewPetRequestDto;
import ps.com.viajeros.dtos.pet.PetResponseDto;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.pet.PetEntity;
import ps.com.viajeros.entities.pet.SizePetEntity;
import ps.com.viajeros.entities.pet.TypePetEntity;
import ps.com.viajeros.repository.pet.PetRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.pet.SizePetRepository;
import ps.com.viajeros.repository.pet.TypePetRepository;
import ps.com.viajeros.services.PetService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SizePetRepository sizePetRepository;

    @Autowired
    private TypePetRepository typePetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper; // Para mapear entidades a DTOs

    @Override
    public List<PetResponseDto> getAllPetsFromUser(Long idUser) {
        // Buscar el usuario por ID
        UserEntity userEntity = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener las mascotas asociadas al usuario
        List<PetEntity> pets = petRepository.findByUser(userEntity);

        // Convertir la lista de PetEntity a PetResponseDto
        return pets.stream()
                .filter(pet -> !pet.isDeleted())
                .map(this::convertToPetResponseDto)
                .collect(Collectors.toList());
    }



    @Override
    public PetResponseDto createNewPet(NewPetRequestDto newPetRequestDto) {
        SizePetEntity size = sizePetRepository.findById(newPetRequestDto.getIdSize())
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado"));
        TypePetEntity type = typePetRepository.findById(newPetRequestDto.getIdType())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        PetEntity petEntity = new PetEntity();
        petEntity.setCanil(newPetRequestDto.isCanil());
        petEntity.setSize(size);
        petEntity.setType(type);
        petEntity.setName(newPetRequestDto.getName());
        petEntity.setUser(userRepository.findById(newPetRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

        PetEntity savedPet = petRepository.save(petEntity);
        return modelMapper.map(savedPet, PetResponseDto.class);
    }

    @Override
    public PetResponseDto updatePet(Long petId, PetResponseDto updatedPetRequest) {
        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        SizePetEntity size = sizePetRepository.findById(updatedPetRequest.getIdSize())
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado"));
        TypePetEntity type = typePetRepository.findById(updatedPetRequest.getIdType())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        petEntity.setCanil(updatedPetRequest.isCanil());
        petEntity.setSize(size);
        petEntity.setType(type);
        petEntity.setName(updatedPetRequest.getName());

        PetEntity updatedPet = petRepository.save(petEntity);
        return modelMapper.map(updatedPet, PetResponseDto.class);
    }

    @Override
    public PetResponseDto deletePet(Long petId) {
        // Buscar la entidad Pet por su ID
        Optional<PetEntity> petEntityOptional = petRepository.findById(petId);

        // Si la entidad está presente, marcamos como 'deleted = true'
        if (petEntityOptional.isPresent()) {
            PetEntity petEntity = petEntityOptional.get();
            petEntity.setDeleted(true);

            // Guardar los cambios en la base de datos
            PetEntity pet = petRepository.save(petEntity);
            return modelMapper.map(pet, PetResponseDto.class);
        } else {
            // Si no se encuentra, lanzar una excepción o manejar el error
            throw new RuntimeException("Pet not found with ID: " + petId);
        }
    }


    private PetResponseDto convertToPetResponseDto(PetEntity petEntity) {
        return modelMapper.map(petEntity, PetResponseDto.class);
    }
}
