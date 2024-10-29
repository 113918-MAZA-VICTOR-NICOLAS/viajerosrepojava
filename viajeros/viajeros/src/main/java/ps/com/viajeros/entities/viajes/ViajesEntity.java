package ps.com.viajeros.entities.viajes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.payment.PaymentEntity;
import ps.com.viajeros.entities.chat.ChatEntity;
import ps.com.viajeros.entities.user.UserEntity;
import ps.com.viajeros.entities.VehicleEntity;
import ps.com.viajeros.entities.viajes.directions.LocalidadEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Viajes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViajesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Long idViaje;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @JsonIgnore
    private VehicleEntity vehiculo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "viaje_pasajeros",
            joinColumns = @JoinColumn(name = "id_viaje"),
            inverseJoinColumns = @JoinColumn(name = "id_pasajero")
    )
    private List<UserEntity> pasajeros;
    @Column(name = "macotas", nullable = false)
    private boolean mascotas;

    @Column(name = "fumar", nullable = false)
    private boolean fumar;


    @Column(name = "gasto_total", nullable = false)
    private BigDecimal gastoTotal;

    @Column(name = "asientos_disponibles", nullable = false)
    private int asientosDisponibles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    @JsonIgnore
    private StatusEntity estado;

    @Column(name = "recordatorio_enviado", nullable = false)
    private boolean recordatorioEnviado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chofer", nullable = false)
    @JsonIgnore
    private UserEntity chofer;

    // Relación con LocalidadEntity para localidad de inicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_inicio", nullable = false)
    private LocalidadEntity localidadInicio;

    // Relación con LocalidadEntity para localidad de fin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_fin", nullable = false)
    private LocalidadEntity localidadFin;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatEntity> chats;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentEntity> pagos; // Lista de pagos realizados para este viaje


    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IncidenteEntity> incidentes;
}
