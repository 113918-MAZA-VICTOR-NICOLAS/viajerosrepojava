package ps.com.viajeros.services;



import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ps.com.viajeros.entities.notification.NotificationEntity;
import ps.com.viajeros.entities.notification.NotificationStatus;
import ps.com.viajeros.entities.notification.NotificationType;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.viajes.StatusEntity;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.NotificationRepository;
import ps.com.viajeros.repository.StatusViajeRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.impl.EmailService;


import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;

@Service
public class ViajeStatusScheduler {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private StatusViajeRepository statusRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    @Scheduled(fixedRate = 30000) // Ejecutar cada 30 segundos
    public void checkAndUpdateViajeStatus() {
        // Obtener el status "in progress" (id_status = 3)
        StatusEntity inProgressStatus = statusRepository.findById(3L)
                .orElseThrow(() -> new IllegalArgumentException("Status 'in progress' no encontrado"));

        // Obtener el status "scheduled" (id_status = 2)
        StatusEntity scheduledStatus = statusRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Status 'scheduled' no encontrado"));

        // Buscar todos los viajes que están en estado "scheduled" y cuya fecha de inicio es anterior o igual a la fecha actual
        List<ViajesEntity> viajes = viajeRepository.findByEstadoAndFechaHoraInicioLessThanEqualFetchPasajeros(scheduledStatus, LocalDateTime.now());

        // Ahora buscar viajes que comiencen dentro de los próximos 1 minutos y una hora
        List<ViajesEntity> viajesEnUnaHora = viajeRepository.findByEstadoAndFechaHoraInicioBetweenFetchPasajeros(
                scheduledStatus, LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusHours(1)
        );

        // Enviar recordatorios a pasajeros
        // Enviar recordatorios a pasajeros y al chofer
        for (ViajesEntity viaje : viajesEnUnaHora) {
            // Solo enviar recordatorio si no ha sido enviado previamente
            if (!viaje.isRecordatorioEnviado()) {

                // Calcular el tiempo restante hasta el inicio del viaje
                Duration duration = Duration.between(LocalDateTime.now(), viaje.getFechaHoraInicio());
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;

                // Construir el mensaje dinámico basado en el tiempo restante
                String tiempoRestante = hours > 0
                        ? " en " + hours + " hora" + (hours > 1 ? "s" : "") + " y " + minutes + " minuto" + (minutes != 1 ? "s" : "") + "."
                        : " en " + minutes + " minuto" + (minutes != 1 ? "s" : "") + ".";

                // Crear el cuerpo del mensaje
                String viajeDetails = "Estimado,\n\nSu viaje desde " + viaje.getLocalidadInicio().getLocalidad() +
                        " hacia " + viaje.getLocalidadFin().getLocalidad() + " comenzará" + tiempoRestante +
                        "\n\nPor favor prepárese y llegue a tiempo.\n\nSaludos,\nViajeros.com";

                // Enviar el recordatorio a cada pasajero
                for (UserEntity pasajero : viaje.getPasajeros()) {
                    emailService.sendReminderEmail(pasajero.getEmail(), viajeDetails);
                    System.out.println("Recordatorio enviado a " + pasajero.getEmail() + " para el viaje con id " + viaje.getIdViaje());
                }

                // Enviar el recordatorio al chofer
                emailService.sendReminderEmail(viaje.getChofer().getEmail(), viajeDetails);
                System.out.println("Recordatorio enviado al chofer " + viaje.getChofer().getEmail() + " para el viaje con id " + viaje.getIdViaje());

                // Marcar el viaje como "recordatorio enviado"
                viaje.setRecordatorioEnviado(true);
                viajeRepository.save(viaje); // Guardar el cambio
            }
        }


        // Finalmente, cambiar el estado de los viajes a "in progress"
        // Finalmente, cambiar el estado de los viajes a "in progress"
        for (ViajesEntity viaje : viajes) {
            viaje.setEstado(inProgressStatus);
            viajeRepository.save(viaje); // Guardar el cambio

            // Crear una notificación para el chofer
            NotificationEntity choferNotification = new NotificationEntity();
            choferNotification.setUser(viaje.getChofer()); // Usuario es el chofer del viaje
            choferNotification.setMessage("Tu viaje hasta " + viaje.getLocalidadFin().getProvincia().getProvincia() + " ha comenzado.");
            choferNotification.setStatus(NotificationStatus.UNREAD);
            choferNotification.setTimestamp(LocalDateTime.now());
            choferNotification.setType(NotificationType.TRIP_STARTED);
            notificationRepository.save(choferNotification); // Guardar notificación para el chofer

            // Crear notificaciones para cada pasajero
            for (UserEntity pasajero : viaje.getPasajeros()) {
                NotificationEntity pasajeroNotification = new NotificationEntity();
                pasajeroNotification.setUser(pasajero); // Usuario es el pasajero del viaje
                pasajeroNotification.setMessage("El viaje hacia " + viaje.getLocalidadFin().getProvincia().getProvincia() + " en el que eres pasajero ha comenzado.");
                pasajeroNotification.setStatus(NotificationStatus.UNREAD);
                pasajeroNotification.setTimestamp(LocalDateTime.now());
                pasajeroNotification.setType(NotificationType.TRIP_STARTED);
                notificationRepository.save(pasajeroNotification); // Guardar notificación para cada pasajero
            }

            System.out.println("El viaje con id " + viaje.getIdViaje() + " ha sido marcado como 'in progress'.");
        }

    }



}
