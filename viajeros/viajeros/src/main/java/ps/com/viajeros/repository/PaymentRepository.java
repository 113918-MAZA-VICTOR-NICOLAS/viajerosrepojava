package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentId(String paymentId);
    Optional<PaymentEntity> findByViajeAndPasajero(ViajesEntity viaje, UserEntity pasajero);
    @Query("SELECT " +
            "SUM(CASE WHEN p.statusPagosChofer = 'PAID' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.statusPagosChofer = 'PENDING' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.statusPagosChofer = 'REJECTED' THEN 1 ELSE 0 END) " +
            "FROM PaymentEntity p")
    List<Object[]> findPaymentStatistics();


}
