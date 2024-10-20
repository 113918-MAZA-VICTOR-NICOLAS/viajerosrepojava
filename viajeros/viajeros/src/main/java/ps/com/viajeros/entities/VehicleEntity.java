package ps.com.viajeros.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.user.UserEntity;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_car")
    private Long idCar;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;

    @Column(name = "patent")
    private String patent;

    @Column(name = "color")
    private String color;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "gnc")
    private boolean gnc;

    @Column(name = "kml")
    private String kmL;

    @Column(name = "deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
