package com.quantumzone.QZ_Workhub.persistencia.repositorio;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    /**
     * Buscar por cantidad de visitantes
     */
    Optional<List<Reserva>> findByCantidadVisitantes(Integer cantidadVisitantes);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reserva r
        WHERE r.sala.idSala = :idSala
          AND (r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio)
    """)
    boolean existeConflicto(@Param("idSala") Long idSala,
                            @Param("fechaInicio") LocalDateTime fechaInicio,
                            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reserva r
        WHERE r.sala.idSala = :idSala
          AND r.idReserva <> :idReserva
          AND (r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio)
    """)
    boolean existeConflictoExcluyendoActual(@Param("idReserva") Long idReserva,
                                            @Param("idSala") Long idSala,
                                            @Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin);
}


