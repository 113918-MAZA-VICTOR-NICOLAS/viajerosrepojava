package ps.com.viajeros.entities.viajes;

public enum EstadoResolucion {
    PENDIENTE,    // El incidente aún no ha sido revisado o resuelto
    RESUELTO,     // El incidente ha sido resuelto
    EN_PROGRESO,  // El incidente está siendo gestionado
    RECHAZADO     // El incidente fue rechazado y no se le dará seguimiento
}
