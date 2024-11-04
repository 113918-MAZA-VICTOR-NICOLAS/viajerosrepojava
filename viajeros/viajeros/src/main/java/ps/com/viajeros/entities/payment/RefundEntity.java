package ps.com.viajeros.entities.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Refunds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refund")
    private Long idRefund;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "refund_mode")
    private String refundMode;

    @Column(name = "reason")
    private String reason;

    @Column(name = "amount_refunded_to_payer", nullable = false)
    private Double amountRefundedToPayer;

    // Relación One-to-One con ReintegroEntity
    @OneToOne(mappedBy = "refund", fetch = FetchType.LAZY)
    private ReintegroEntity reintegro;
}