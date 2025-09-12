package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
//imports de conexion base de datos
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;

@Repository
public interface RecursoReservadoRepository extends  JpaRepository<RecursoReservado,Long> {
    /**
     *  Por resolver
     */
}
