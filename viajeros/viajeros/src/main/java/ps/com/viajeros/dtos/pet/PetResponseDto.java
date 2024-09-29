package ps.com.viajeros.dtos.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetResponseDto {
    private Long idPet;
    private boolean deleted;
    private boolean canil;
    private Long idSize;  // Nombre del tamaño, por ejemplo, "Chico", "Mediano", "Grande"
    private Long idType;  // Tipo del animal, por ejemplo, "Perro", "Gato"
    private Long userId;      // ID del usuario asociado
    private String name;
}
