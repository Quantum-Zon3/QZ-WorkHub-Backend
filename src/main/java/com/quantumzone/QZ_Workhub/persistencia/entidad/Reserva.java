package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva", nullable = false)
    private Integer idReserva;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "monto_total", length = 45)
    private String montoTotal;

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

    // Constructor vacío (obligatorio para JPA)
    public Reserva() {}

    // Constructor con parámetros
    public Reserva(LocalDateTime fechaInicio, LocalDateTime fechaFin, String montoTotal,
                   Integer cantidadVisitantes, Usuario usuario, Sala sala) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoTotal = montoTotal;
        this.cantidadVisitantes = cantidadVisitantes;
        this.usuario = usuario;
        this.sala = sala;
    }

    // Getters y Setters
    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getCantidadVisitantes() {
        return cantidadVisitantes;
    }

    public void setCantidadVisitantes(Integer cantidadVisitantes) {
        this.cantidadVisitantes = cantidadVisitantes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }

    public List<RecursoReservado> getRecursosReservados() {
        return recursosReservados;
    }

    public void setRecursosReservados(List<RecursoReservado> recursosReservados) {
        this.recursosReservados = recursosReservados;
    }

}