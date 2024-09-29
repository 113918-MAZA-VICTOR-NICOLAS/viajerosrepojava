package ps.com.viajeros.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.pet.PetEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "viajeros_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token", nullable = true)
    private String token;

    @Column(name = "token_expiration", nullable = true)
    private LocalDateTime tokenExpiration;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "bank")
    private String bank;

    @Column(name = "cbu")
    private String cbu;

    @Column(name = "cuil")
    private String cuil;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleEntity> vehicles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PetEntity> pets;

    // Relación Uno a Muchos con ValuationEntity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValuationEntity> valuations;
}