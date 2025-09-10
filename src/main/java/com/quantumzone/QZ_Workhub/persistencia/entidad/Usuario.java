package com.quantumzone.QZ_Workhub.persistencia.entidad;
import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
// imports de la persitencia
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "cedula", nullable = false)
    private Long cedula;
    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 45, nullable = false)
    private String apellido;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @Column(name = "contraseña", length = 45, nullable = false)
    private String contraseña;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "telefono", length = 45, nullable = false)
    private String telefono;

    // Relaciones
    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario")
    private List<Reporte> reportes;

    // Constructor vacío
    public Usuario() {}

    // Constructor con parámetros
    public Usuario(Long cedula, String nombre, String apellido, String email, Rol rol,
                   String contraseña, LocalDateTime fechaRegistro, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        this.contraseña = contraseña;
        this.fechaRegistro = fechaRegistro;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }
}
