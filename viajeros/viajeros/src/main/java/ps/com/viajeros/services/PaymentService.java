package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.payments.PaymentDto;
import ps.com.viajeros.dtos.payments.PreferenceTripDto;
import ps.com.viajeros.dtos.payments.ResponsePaymentDto;

@Service
public interface PaymentService {
    void registerPayment(PaymentDto paymentDto);

    void requestReintegroByPassenger(Long tripId, Long userId);

    ResponsePaymentDto getPaymentById(Long id);
}
