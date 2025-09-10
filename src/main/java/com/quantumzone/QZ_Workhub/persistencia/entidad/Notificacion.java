package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion", nullable = false)
    private Integer idNotificacion;

    @Column(name = "motivo", length = 45, nullable = false)
    private String motivo;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;


    public Notificacion() {}

    // Constructor con parámetros
    public Notificacion(String motivo, LocalDateTime fecha, String descripcion, Reserva reserva) {
        this.motivo = motivo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.reserva = reserva;
    }

    // Getters y Setters
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}