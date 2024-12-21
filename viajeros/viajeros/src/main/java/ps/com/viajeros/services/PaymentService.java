package ps.com.viajeros.services;

import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.payments.*;

import java.util.List;

@Service
public interface PaymentService {
    void registerPayment(PaymentDto paymentDto);

    void requestReintegroByPassenger(Long tripId, Long userId);

    ResponsePaymentDto getPaymentById(Long id);

    List<PagoPasajeroDto> obtenerPagosPasajeros();

    void actualizarEstadoPagoChofer(RequestDriverPaymentDto request);

    PaymentStadisticDto getStatusNumPayments();
}
