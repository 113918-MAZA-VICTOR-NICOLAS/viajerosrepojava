package ps.com.viajeros.entities.viajes.directions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "provincias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProvinciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "provincia", nullable = false)
    private String provincia;
}
