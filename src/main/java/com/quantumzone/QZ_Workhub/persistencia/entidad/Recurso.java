package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recurso")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso", nullable = false)
    private Integer idRecurso;

    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoRecurso tipo;

    @Column(name = "unidades", nullable = false)
    private Integer unidades;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Float precio;

    // Relaciones
    @OneToMany(mappedBy = "recurso")
    private List<RecursoReservado> recursoReservados;
    // Constructor vacío (obligatorio para JPA)
    public Recurso() {}

    // Constructor con parámetros
    public Recurso(String nombre, TipoRecurso tipo, Integer unidades, String descripcion, Float precio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.unidades = unidades;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
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

    public List<RecursoReservado> getRecursoReservados() {
        return recursoReservados;
    }

    public void setRecursoReservados(List<RecursoReservado> recursoReservados) {
        this.recursoReservados = recursoReservados;
    }
}