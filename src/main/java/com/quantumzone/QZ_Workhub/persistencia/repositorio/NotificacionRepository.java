package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import jakarta.persistence.EntityManager;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
//imports de conexion base de datos
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    /**
    * Buscar notifiacion por reserva
    */
    Optional<List<Notificacion>> findByReserva(Reserva reserva);
    /**
     * Buscar notificacion del Mienbro
     */
    Optional<List<Notificacion>> findByReservaUsuarioCedula(Long cedula);
}
