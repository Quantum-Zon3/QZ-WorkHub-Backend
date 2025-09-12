package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurso")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso", nullable = false)
    private Long idRecurso;

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


    @OneToMany(mappedBy = "recurso")
    private List<RecursoReservado> recursoReservados;

}