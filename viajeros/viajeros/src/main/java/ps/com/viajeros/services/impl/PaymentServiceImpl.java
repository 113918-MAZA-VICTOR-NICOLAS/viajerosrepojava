package ps.com.viajeros.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.payments.*;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.payment.ReintegroEntity;
import ps.com.viajeros.entities.payment.ReintegroMotivo;
import ps.com.viajeros.entities.payment.ReintegroStatus;
import ps.com.viajeros.entities.viajes.ViajesEntity;
import ps.com.viajeros.repository.PaymentRepository;
import ps.com.viajeros.repository.ReintegroRepository;
import ps.com.viajeros.repository.UserRepository;
import ps.com.viajeros.repository.ViajeRepository;
import ps.com.viajeros.services.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ResponsePaymentDto getPaymentById(Long id) {
        return paymentRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public List<PagoPasajeroDto> obtenerPagosPasajeros() {
        return paymentRepository.findAll().stream()
                .map(payment -> new PagoPasajeroDto(
                        payment.getIdPayment(),
                        payment.getViaje().getIdViaje(),
                        payment.getPasajero().getName(),
                        BigDecimal.valueOf(2000L), // Suponiendo que hay un campo de importe
                        payment.getStatus(),
                        payment.getViaje().getChofer().getName(),
                        payment.getViaje().getChofer().getCbu(),
                        payment.getViaje().getChofer().getBank(),
                        payment.getStatusPagosChofer() != null ? payment.getStatusPagosChofer().toString() : "PENDING"
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarEstadoPagoChofer(RequestDriverPaymentDto request) {
        PaymentEntity payment = paymentRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + request.getIdPago()));

        // Actualizar el estado del pago del chofer y la fecha de pago si es necesario
        payment.setStatusPagosChofer(StatusPagosChofer.valueOf(request.getEstado()));
        payment.setIdPagoAlChofer(request.getIdTransferenciaChofer());
        payment.setFechaPagoAlChofer(LocalDateTime.now()); // Asigna la fecha actual como fecha de pago al chofer

        paymentRepository.save(payment);
    }

    @Override
    public PaymentStadisticDto getStatusNumPayments() {
        List<Object[]> result = paymentRepository.findPaymentStatistics();

        if (result == null || result.isEmpty() || result.get(0) == null) {
            return PaymentStadisticDto.builder()
                    .paidCount(0L)
                    .paidTotal(0L)
                    .pendingCount(0L)
                    .pendingTotal(0L)
                    .rejectedCount(0L)
                    .rejectedTotal(0L)
                    .build();
        }

        Object[] stats = result.get(0);
        Long paidCount = ((Number) stats[0]).longValue();    // Número de pagos PAID
        Long pendingCount = ((Number) stats[1]).longValue(); // Número de pagos PENDING
        Long rejectedCount = ((Number) stats[2]).longValue(); // Número de pagos REJECTED

        // Multiplicar por el monto unitario (2000)
        Long paidTotal = paidCount * 2000;
        Long pendingTotal = pendingCount * 2000;
        Long rejectedTotal = rejectedCount * 2000;

        return PaymentStadisticDto.builder()
                .paidCount(paidCount)
                .paidTotal(paidTotal)
                .pendingCount(pendingCount)
                .pendingTotal(pendingTotal)
                .rejectedCount(rejectedCount)
                .rejectedTotal(rejectedTotal)
                .build();
    }





    private ResponsePaymentDto convertToDto(PaymentEntity payment) {
        ResponsePaymentDto dto = new ResponsePaymentDto();
        dto.setIdPayment(payment.getIdPayment());
        dto.setPaymentId(payment.getPaymentId());
        dto.setStatus(payment.getStatus());
        dto.setExternalReference(payment.getExternalReference());
        dto.setPaymentType(payment.getPaymentType());
        dto.setMerchantOrderId(payment.getMerchantOrderId());
        dto.setIdPasajero(payment.getPasajero().getIdUser());
        dto.setNombreCompletoPasajero(payment.getPasajero().getName() + " " + payment.getPasajero().getLastname());
        dto.setCuilPasajero(payment.getPasajero().getCuil());
        dto.setCbuPasajero(payment.getPasajero().getCbu());
        dto.setFechaPago(payment.getFechaPago());
        return dto;
    }


}
