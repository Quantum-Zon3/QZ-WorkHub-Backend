package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    /**
     * Buscar por fecha
     */
    Optional<List<Reporte>> findByFecha(LocalDateTime fecha);
}
