package com.quantumzone.QZ_Workhub.persistencia.entidad;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol_asignado")
public class RolAsignado {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fecha_asignada", nullable = false)
    private LocalDateTime fechaAsignada;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Usuario usuario;

}
