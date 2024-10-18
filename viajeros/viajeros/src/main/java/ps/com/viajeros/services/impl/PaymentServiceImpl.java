package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.payments.PaymentDto;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.UserEntity;
import ps.com.viajeros.entities.payment.ReintegroEntity;
import ps.com.viajeros.entities.payment.ReintegroMotivo;
import ps.com.viajeros.entities.payment.ReintegroStatus;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.PaymentRepository;
import ps.com.viajeros.repository.ReintegroRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.PaymentService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ViajeRepository viajesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReintegroRepository reintegroRepository;

    @Override
    public void registerPayment(PaymentDto paymentDto) {
        // Buscar el viaje y el pasajero por sus IDs
        ViajesEntity viaje = viajesRepository.findById(paymentDto.getIdViaje())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        UserEntity pasajero = userRepository.findById(paymentDto.getIdPasajero())
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

        // Verificar si el pago ya fue registrado usando el paymentId
        Optional<PaymentEntity> paymentrep = paymentRepository.findByPaymentId(paymentDto.getPaymentId());

        if (paymentrep.isPresent()) {
            throw new RuntimeException("Este pago ya fue registrado");
        }

        // Verificar el estado del pago
        String paymentStatus = paymentDto.getStatus();
        if ("rejected".equalsIgnoreCase(paymentStatus) || paymentStatus == null) {
            // Si el pago es rechazado, lanzar una excepción o manejar el error
            throw new RuntimeException("Pago rechazado, no se puede asignar el pasajero al viaje");
        }

        // Crear una nueva entidad PaymentEntity
        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentId(paymentDto.getPaymentId());
        payment.setStatus(paymentDto.getStatus());
        payment.setExternalReference(paymentDto.getExternalReference());
        payment.setPaymentType(paymentDto.getPaymentType());
        payment.setMerchantOrderId(paymentDto.getMerchantOrderId());
        payment.setPasajero(pasajero);
        payment.setViaje(viaje);
        payment.setFechaPago(LocalDateTime.now());

        // Guardar el pago en la base de datos
        paymentRepository.save(payment);

        // Si el estado es 'approved' o 'pending', asignar el pasajero al viaje
        if ("approved".equalsIgnoreCase(paymentStatus) || "pending".equalsIgnoreCase(paymentStatus)) {
            // Agregar el pasajero a la lista de pasajeros del viaje
            viaje.getPasajeros().add(pasajero);

            // Disminuir el número de asientos disponibles
            if (viaje.getAsientosDisponibles() > 0) {
                viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
            } else {
                throw new RuntimeException("No hay asientos disponibles en el viaje");
            }

            // Guardar el viaje actualizado
            viajesRepository.save(viaje);
        }
    }

    @Override
    public void requestReintegroByPassenger(Long tripId, Long userId) {
        // Buscar el viaje y el pasajero
        ViajesEntity viaje = viajesRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        UserEntity pasajero = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado"));

        // Buscar el pago del pasajero para el viaje
        PaymentEntity payment = paymentRepository.findByViajeAndPasajero(viaje, pasajero)
                .orElseThrow(() -> new RuntimeException("No se encontró el pago para este pasajero en el viaje"));

        // Verificar si ya existe un reintegro para este pago
        if (payment.getReintegro() != null) {
            throw new RuntimeException("Ya se ha solicitado un reintegro para este pago");
        }

        // Crear una nueva entidad de reintegro
        ReintegroEntity reintegro = new ReintegroEntity();
        reintegro.setPayment(payment);  // Asociar el reintegro con el pago
        reintegro.setStatus(ReintegroStatus.REQUIRED);  // Establecer el estado de reintegro a "REQUIRED"
        reintegro.setFechaReintegro(LocalDateTime.now());  // Fecha de la solicitud de reintegro
        reintegro.setReintegroMotivo(ReintegroMotivo.PASSENGER_CANCEL);  // Motivo del reintegro

        // Guardar el reintegro
        reintegroRepository.save(reintegro);

        // Actualizar el estado del pago para reflejar la solicitud de reintegro
        payment.setReintegro(reintegro);  // Relacionar el pago con el reintegro
        paymentRepository.save(payment);  // Guardar los cambios en el pago
    }

}
