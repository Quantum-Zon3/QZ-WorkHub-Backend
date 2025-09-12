package com.quantumzone.QZ_Workhub.persistencia.entidad;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
