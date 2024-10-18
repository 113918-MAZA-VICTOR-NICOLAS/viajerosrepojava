package ps.com.viajeros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.payment.ReintegroEntity;

@Repository
public interface ReintegroRepository extends JpaRepository<ReintegroEntity, Long> {
}
