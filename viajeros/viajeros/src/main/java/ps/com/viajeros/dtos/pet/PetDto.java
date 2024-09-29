package ps.com.viajeros.dtos.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDto {
    private Long idPet;
    private String name;
    private boolean deleted;
    private boolean canil;
    private String size;
    private String type;
}
