package com.quantumzone.QZ_Workhub.persistencia.entidad;
// imports de la persitencia
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurso_reservado")
public class RecursoReservado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id auto generada
    private Long id;
    //atributos por definir

}
