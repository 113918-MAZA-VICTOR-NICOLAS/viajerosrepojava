package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.dtos.payments.PaymentDto;
import ps.com.viajeros.dtos.payments.ResponsePaymentDto;
import ps.com.viajeros.services.PaymentService;

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
}
