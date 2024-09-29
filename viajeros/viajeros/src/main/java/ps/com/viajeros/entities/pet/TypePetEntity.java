package ps.com.viajeros.entities.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_pet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypePetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type")
    private Long idType;

    @Column(name = "type_name")
    private String typeName;  // Por ejemplo: "Gato", "Perro", "Otro"
}
