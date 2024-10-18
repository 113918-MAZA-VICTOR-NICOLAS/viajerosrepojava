package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentId(String paymentId);
    Optional<PaymentEntity> findByViajeAndPasajero(ViajesEntity viaje, UserEntity pasajero);

}
