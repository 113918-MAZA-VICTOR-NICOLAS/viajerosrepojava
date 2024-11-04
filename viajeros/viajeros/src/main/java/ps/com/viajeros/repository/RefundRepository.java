package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.payment.RefundEntity;

@Repository
public interface RefundRepository  extends JpaRepository<RefundEntity, Long> {
}
