package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.pet.NewPetRequestDto;
import ps.com.viajeros.dtos.pet.PetResponseDto;
import ps.com.viajeros.services.PetService;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    // Obtener todas las mascotas de un usuario específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PetResponseDto>> getAllPetsByUser(@PathVariable Long userId) {
        List<PetResponseDto> pets = petService.getAllPetsFromUser(userId);
        return ResponseEntity.ok(pets);
    }

    // Registrar una nueva mascota
    @PostMapping("/create")
    public ResponseEntity<PetResponseDto> createNewPet(@RequestBody NewPetRequestDto newPetRequestDto) {
        PetResponseDto newPet = petService.createNewPet(newPetRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPet);
    }

    // Actualizar una mascota existente
    @PutMapping("/update/{petId}")
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable Long petId,
            @RequestBody PetResponseDto updatedPetRequest) {
        PetResponseDto updatedPet = petService.updatePet(petId, updatedPetRequest);
        return ResponseEntity.ok(updatedPet);
    }
    // Eliminar una mascota por su ID
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<PetResponseDto> deletePet(@PathVariable Long petId) {
        PetResponseDto petResponseDto = petService.deletePet(petId);
        return ResponseEntity.ok(petResponseDto);  // Devolver la respuesta con el DTO
    }

}
