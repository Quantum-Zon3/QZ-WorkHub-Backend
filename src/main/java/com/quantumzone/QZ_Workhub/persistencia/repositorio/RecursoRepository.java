package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
//imports de conexion base de datos
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;

@Repository
public interface RecursoRepository extends  JpaRepository<Recurso, Long> {
    /**
     * Buscar recurso por tipo (tecnologico ó mueble)
     */
    List<Recurso> findRecursosByTipo(TipoRecurso tipo);
}
