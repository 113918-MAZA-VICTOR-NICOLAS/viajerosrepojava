package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.payments.PagoPasajeroDto;
import ps.com.viajeros.dtos.payments.PaymentDto;
import ps.com.viajeros.dtos.payments.RequestDriverPaymentDto;
import ps.com.viajeros.dtos.payments.ResponsePaymentDto;
import ps.com.viajeros.services.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentsController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/register-payment")
    public ResponseEntity<String> registerPayment(@RequestBody PaymentDto paymentDto) {
        try {
            paymentService.registerPayment(paymentDto);
            return ResponseEntity.ok("Pago registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el pago");
        }
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<ResponsePaymentDto> getPaymentById(@PathVariable Long id) {
        ResponsePaymentDto payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/listado-pasajeros")
    public ResponseEntity<List<PagoPasajeroDto>> obtenerListadoPagos() {
        List<PagoPasajeroDto> pagos = paymentService.obtenerPagosPasajeros();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
    }

    @PutMapping("/actualizar-estado")
    public ResponseEntity<String> actualizarEstadoPagoChofer(@RequestBody RequestDriverPaymentDto request) {
        paymentService.actualizarEstadoPagoChofer(request);
        return ResponseEntity.ok("Estado del pago del chofer actualizado exitosamente.");
    }
}
