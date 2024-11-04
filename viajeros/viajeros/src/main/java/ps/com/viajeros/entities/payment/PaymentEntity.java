package ps.com.viajeros.entities.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.dtos.payments.StatusPagosChofer;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long idPayment;

    @Column(name = "payment_id", nullable = false)
    private String paymentId; // Este es el ID del pago proporcionado por MercadoPago

    @Column(name = "status", nullable = false)
    private String status; // Estado del pago (approved, pending, etc.)

    @Column(name = "external_reference", nullable = false)
    private String externalReference; // Referencia externa del pago

    @Column(name = "payment_type", nullable = false)
    private String paymentType; // Tipo de pago (credit_card, etc.)

    @Column(name = "merchant_order_id", nullable = false)
    private String merchantOrderId; // ID de la orden del comerciante

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajero", nullable = false)
    private UserEntity pasajero; // Relación con el pasajero que realizó el pago

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje", nullable = false)
    private ViajesEntity viaje; // Relación con el viaje al cual corresponde el pago

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago; // Fecha y hora en que se realizó el pago

    @Column(name = "fecha_pago_chofer", nullable = false)
    private LocalDateTime fechaPagoAlChofer; // Fecha y hora en que se realizó el pago

    @Column(name = "id_pago_chofer", nullable = false)
    private Long idPagoAlChofer; // Fecha y hora en que se realizó el pago
    // Relación One-to-One con ReintegroEntity

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pago_chofer", nullable = false)
    private StatusPagosChofer statusPagosChofer;


    // Relación One-to-One con ReintegroEntity
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ReintegroEntity reintegro; // Un pago tiene un reintegro
    // Método para establecer el estado del pago del chofer como "PENDING" por defecto
    @PrePersist
    private void prePersist() {
        if (statusPagosChofer == null) {
            statusPagosChofer = StatusPagosChofer.PENDING;
        }
    }
}
