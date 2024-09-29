package ps.com.viajeros.entities.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "size_pet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizePetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_size")
    private Long idSize;

    @Column(name = "size_name")
    private String sizeName;  // Por ejemplo: "Chico", "Mediano", "Grande"
}
