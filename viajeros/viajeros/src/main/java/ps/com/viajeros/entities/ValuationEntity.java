package ps.com.viajeros.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "valuation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valuation")
    private Long idValuation;

    @Column(name = "id_trip")
    private Long idTrip;

    @Column(name = "comments")
    private String comments;

    @Column(name = "rating")
    private Long rating;

    // Relación Muchos a Uno con UserEntity
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;


}
