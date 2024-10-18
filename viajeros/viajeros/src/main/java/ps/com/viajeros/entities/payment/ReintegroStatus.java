package ps.com.viajeros.entities.payment;

public enum ReintegroStatus {
    NULL, // No se ha solicitado reintegro
    REQUIRED, // Reintegro solicitado
    RETURNED, // Reintegro completado
    REJECTED // Reintegro rechazado
}
