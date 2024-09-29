package ps.com.viajeros.entities.pet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.UserEntity;

@Entity
@Table(name = "pet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Long idPet;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "canil")
    private boolean canil;

    // Relación con la tabla SizeEntity
    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id_size")
    private SizePetEntity size;

    // Relación con la tabla TypeEntity
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id_type")
    private TypePetEntity type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}

