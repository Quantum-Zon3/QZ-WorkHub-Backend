package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.List;
import java.util.Optional;

import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
//imports de conexion base de datos
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    /**
     * Busca salas por nombre
     */
    Optional<List<Sala>> findByNombre(String nombre);
}