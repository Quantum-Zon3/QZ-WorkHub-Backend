package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurso_reservado")
public class RecursoReservado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id auto generada
    @Column(name = "id_recuso_reservado", nullable = false)
    private Long id;
    @Column(name = "cantidad" , nullable = false)
    private Long cantidad;
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    @Column(name = "monto_total", nullable = false)
    private int montoTotal;
    @ManyToOne
    @JoinColumn(name = "id_recurso", nullable = false)
    private Recurso recurso;
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

}
