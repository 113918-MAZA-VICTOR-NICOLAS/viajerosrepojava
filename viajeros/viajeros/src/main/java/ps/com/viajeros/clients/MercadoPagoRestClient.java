package ps.com.viajeros.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ps.com.viajeros.entities.payment.RefundEntity;
import ps.com.viajeros.repository.RefundRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class MercadoPagoRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RefundRepository refundRepository;
    private String baseUrl = "https://api.mercadopago.com/v1/payments/";
    private String accessToken = "APP_USR-8790603519044330-082821-00f71db557de45062431b4eaca8d0664-62624629"; // Reemplaza con tu token de acceso


    public ResponseEntity<String> realizarReintegro(String paymentId, Double amount) {
        // URL específica para realizar el reembolso
        String url = baseUrl + paymentId + "/refunds";

        // Configura los encabezados de autenticación, tipo de contenido y X-Idempotency-Key
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        // Genera un UUID único para X-Idempotency-Key
        String idempotencyKey = UUID.randomUUID().toString();
        headers.add("X-Idempotency-Key", idempotencyKey);

        // Cuerpo de la solicitud (opcional si es reembolso total)
        String requestBody = amount != null ? "{\"amount\":" + amount + "}" : "{}";

        // Crea la entidad de la solicitud
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Realiza la solicitud POST al endpoint de reembolsos
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Si el reembolso fue exitoso, parsea y guarda la respuesta
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Parseo de la respuesta
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(response.getBody());

                // Extrae los valores necesarios
                RefundEntity refund = new RefundEntity();
                refund.setPaymentId(paymentId);
                refund.setAmount(amount != null ? amount : jsonResponse.path("amount").asDouble());
                refund.setStatus(jsonResponse.path("status").asText());
                refund.setDateCreated(LocalDateTime.now());
                refund.setRefundMode(jsonResponse.path("refund_mode").asText());
                refund.setReason(jsonResponse.path("reason").asText());
                refund.setAmountRefundedToPayer(jsonResponse.path("amount_refunded_to_payer").asDouble());

                // Guarda la entidad RefundEntity en la base de datos
                refundRepository.save(refund);

                // Retorna la respuesta de éxito
                return ResponseEntity.ok("Reintegro realizado con éxito y guardado en la base de datos");
            } else {
                System.err.println("Error al realizar el reintegro, response es nulo o no exitoso");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al realizar el reintegro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error procesando el reintegro: " + e.getMessage());
        }
    }
}