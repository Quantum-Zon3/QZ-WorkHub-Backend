package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "cantidad_visitantes", nullable = false)
    private Integer cantidadVisitantes;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @OneToMany(mappedBy = "reserva")
    private List<Pago> pagos;

    @OneToMany(mappedBy = "reserva")
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "reserva")
    private List<Reporte> reportes;

    @OneToMany(mappedBy = "reserva")
    private List<RecursoReservado> recursosReservados;

    // Relación bidireccional: una reserva tiene un pago
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Pago pago;
}