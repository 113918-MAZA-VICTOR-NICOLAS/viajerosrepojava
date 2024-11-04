package ps.com.viajeros.controller;


import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.com.viajeros.clients.MercadoPagoRestClient;
import ps.com.viajeros.dtos.payments.PreferenceTripDto;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.payment.ReintegroEntity;
import ps.com.viajeros.entities.payment.ReintegroStatus;
import ps.com.viajeros.repository.PaymentRepository;
import ps.com.viajeros.repository.ReintegroRepository;
import ps.com.viajeros.services.impl.MercadoPagoServiceImpl;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoServiceImpl mercadoPagoService;
    @Autowired
    private MercadoPagoRestClient mercadoPagoRestClient;

    @Autowired
    private ReintegroRepository reintegroRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @PostMapping("/mercadopago/crear-preferencia")
    public Preference crearPreferencia(@RequestBody PreferenceTripDto preferenceTrip) {
        return mercadoPagoService.crearPreferencia(preferenceTrip);
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        // Extraer los valores del payload
        String paymentStatus = (String) payload.get("status");
        String paymentId = (String) payload.get("collection_id");
        String paymentType = (String) payload.get("payment_type");

        // Imprimir los valores en la consola
        System.out.println("Payment ID ID DE MERCADOPAGO: " + paymentId);
        System.out.println("Payment Status ESTADO DE PAGO: " + paymentStatus);
        System.out.println("Payment Type METODO DE PAGO: " + paymentType);
        System.out.println("Full Payload: " + payload); // Para ver todo el contenido del payload

        // Aquí puedes guardar el estado del pago en la base de datos

        // Responder con 200 OK para indicar que se recibió correctamente
        return ResponseEntity.ok("Received");
    }

    // Endpoint para realizar el reintegro
    @PostMapping("/mercadopago/reintegro/{paymentId}")
    public ResponseEntity<String> realizarReintegro(
            @PathVariable String paymentId,
            @RequestParam(required = false) Double amount) {

        System.out.println("Payment ID recibido: " + paymentId);
        System.out.println("Monto a reintegrar recibido: " + amount);

        // Convierte paymentId a Long correctamente
        PaymentEntity payment = paymentRepository.getReferenceById(Long.valueOf(paymentId));

        // Llama al RestClient para realizar el reintegro
        ResponseEntity<String> response = mercadoPagoRestClient.realizarReintegro(payment.getPaymentId(), amount);

        // Verifica si el response es nulo o contiene errores
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            // Si fue exitoso, actualiza el estado del reintegro a RETURNED
            ReintegroEntity reintegroEntity = payment.getReintegro();
            reintegroEntity.setFechaReintegro(LocalDateTime.now());
            reintegroEntity.setStatus(ReintegroStatus.RETURNED);
            payment.setReintegro(reintegroEntity);// Asume que tienes este campo en PaymentEntity o ReintegroEntity
            paymentRepository.save(payment);  // Guarda los cambios

            return ResponseEntity.ok("Reintegro realizado y estado actualizado a RETURNED");
        }

        // Retorna la respuesta de MercadoPago
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}