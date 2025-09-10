package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import com.quantumzone.QZ_Workhub.dominio.enums.MotivoReporte;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte", nullable = false)
    private Integer idReporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoReporte motivo;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_cedula", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    // Constructor vacío (obligatorio para JPA)
    public Reporte() {}

    // Constructor con parámetros
    public Reporte(MotivoReporte motivo, LocalDateTime fecha, Usuario usuario, Reserva reserva) {
        this.motivo = motivo;
        this.fecha = fecha;
        this.usuario = usuario;
        this.reserva = reserva;
    }

    // Getters y Setters
    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public MotivoReporte getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoReporte motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}
