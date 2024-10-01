package ps.com.viajeros.entities.viajes.directions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.viajes.directions.ProvinciaEntity;

@Entity
@Table(name = "localidades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia", nullable = false)
    private ProvinciaEntity provincia;

    @Column(name = "localidad", nullable = false)
    private String localidad;
}
