package com.quantumzone.QZ_Workhub.persistencia.entidad;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala", nullable = false)
    private Integer idSala;

    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "descripcion", length = 45)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Float precio;

    // Relaciones
    @OneToMany(mappedBy = "sala")
    private List<Reserva> reservas;

    // Constructor vacío (obligatorio para JPA)
    public Sala() {}

    // Constructor con parámetros
    public Sala(String nombre, Integer capacidad, String descripcion, Float precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

}
