package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
//imports de conexion base de datos
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago,Long>  {
    /**
     * Buscar pago por Reserva
     */
    Optional<List<Pago>> findByReserva(Reserva reserva);
    /**
     * Buscar pago del Mienbro
     */
    Optional<List<Pago>> findByReservaUsuarioCedula(Long cedula);

}
