package ps.com.viajeros.dtos.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewPetRequestDto {
    private boolean canil;
    private Long sizeId;      // ID del tamaño, por ejemplo, 1 (Chico), 2 (Mediano), etc.
    private Long typeId;      // ID del tipo, por ejemplo, 1 (Perro), 2 (Gato), etc.
    private Long userId;      // ID del usuario asociado
}

