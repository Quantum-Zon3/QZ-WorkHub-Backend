package com.quantumzone.QZ_Workhub.persistencia;
// imports de la persitencia
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id auto generada
    private Long id;
    //atributos por definir
    public Notificacion() {}
}
