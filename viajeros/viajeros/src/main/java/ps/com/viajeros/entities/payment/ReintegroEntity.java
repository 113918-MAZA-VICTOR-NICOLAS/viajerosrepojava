package ps.com.viajeros.entities.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reintegros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReintegroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reintegro")
    private Long idReintegro;

    // Estado del reintegro
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReintegroStatus status = ReintegroStatus.NULL;

    // Administrador que realizó el reintegro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_reintegro")
    private UserEntity adminReintegro;

    // Fecha y hora en que se realizó el reintegro
    @Column(name = "fecha_reintegro")
    private LocalDateTime fechaReintegro;

    // Motivo del reintegro
    @Enumerated(EnumType.STRING)
    @Column(name = "reintegro_motivo")
    private ReintegroMotivo reintegroMotivo;


    // Pago relacionado con este reintegro
    // Relación One-to-One con PaymentEntity
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment", nullable = false)
    private PaymentEntity payment; // Un reintegro está asociado a un solo pago
}
