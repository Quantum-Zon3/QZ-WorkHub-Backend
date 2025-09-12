package com.quantumzone.QZ_Workhub.persistencia.repositorio;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    /**
     * Buscar por cantidad de visitantes
     */
    Optional<List<Reserva>> findByCantidadVisitantes(Integer cantidadVisitantes);
}
