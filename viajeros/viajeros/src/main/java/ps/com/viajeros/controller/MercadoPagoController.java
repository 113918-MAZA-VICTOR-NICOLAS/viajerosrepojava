package ps.com.viajeros.controller;


import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.com.viajeros.dtos.OrderData;
import ps.com.viajeros.services.impl.MercadoPagoServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoServiceImpl mercadoPagoService;

    @PostMapping("/mercadopago/crear-preferencia")
    public Preference crearPreferencia(@RequestBody OrderData orderData) {
        return mercadoPagoService.crearPreferencia(orderData);
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

}