package ps.com.viajeros.entities.viajes;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ps.com.viajeros.entities.user.UserEntity;

import java.time.LocalDateTime;
@Entity
@Table(name = "Incidentes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidente")
    private Long idIncidente;

    // Relación Uno a Uno con ViajesEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje", nullable = false)
    @JsonIgnore  // Ignora la serialización de esta relación
    private ViajesEntity viaje;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidente", nullable = false)
    private TipoIncidente tipoIncidente;

    @Column(name = "fecha_incidente", nullable = false)
    private LocalDateTime fechaIncidente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "denunciado", nullable = false)
    @JsonIgnore  // Ignora la serialización de esta relación
    private UserEntity denunciado;

    @Column(name = "is_pasajero", nullable = false)
    private Boolean isPasajero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportado_por", nullable = false)
    @JsonIgnore  // Ignora la serialización de esta relación
    private UserEntity reportadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_resolucion", nullable = false)
    private EstadoResolucion estadoResolucion;

    // Campo para guardar detalles sobre cómo se resolvió el incidente
    @Column(name = "resolucion", length = 500)
    private String resolucion; // Detalles de la resolución

    // Fecha en la que se resolvió el incidente, opcional
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
}
