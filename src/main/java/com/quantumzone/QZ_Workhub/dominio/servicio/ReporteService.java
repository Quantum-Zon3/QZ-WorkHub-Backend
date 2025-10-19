package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.ReporteDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio de reporte
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @RequiredArgsConstructor - Lombok genera constructor con dependencias final * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de reporteDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de reporte
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class ReporteService {

    private final ReporteDAO reporteDAO;
    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    @Autowired
    public ReporteService(ReporteDAO reporteDAO, UsuarioService usuarioService, ReservaService reservaService) {
        this.reporteDAO = reporteDAO;
        // Inicializamos algunos datos si es necesario
        initSampleData();
        this.usuarioService = usuarioService;
        this.reservaService = reservaService;
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }
    /**
     * Crear un nuevo reporte con validaciones de negocio
     */
    public ReporteDto save(ReporteDto reporteDto) {
        log.info("Creando nuevo reporte con cedula: {}", reporteDto.getIdReporte());
        // Validaciones adicionales de negocio
        validarReporte(reporteDto);
        // Crear reporte
        ReporteDto reporteCreado = reporteDAO.save(reporteDto);
        log.info("reporte creado exitosamente con ID: {}", reporteCreado.getIdReporte());
        return reporteCreado;
    }
    /**
     * Buscar reporte por ID
     */
    @Transactional(readOnly = true)
    public ReporteDto findById(Long id) {
        log.debug("Buscando reporte por ID: {}", id);

        return reporteDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("reporte no encontrado con ID: {}", id);
                    return new RuntimeException("reporte no encontrado con ID: " + id);
                });
    }

    /**
     * Obtener todos las reporte
     */
    @Transactional(readOnly = true)
    public List<ReporteDto> findAll() {
        log.debug("Obteniendo todos los reporte: {}", reporteDAO.findAll().size());
        return reporteDAO.findAll();
    }

    /**
     * Eliminar reporte
     */
    public void deleteReporte(Long id) {
        log.info("Eliminando reporte ID: {}", id);

        // Verificar que el reporte existe
        findById(id);

        // Eliminar reporte
        boolean deleted = reporteDAO.delete(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar reporte con ID: " + id);
        }

        log.info("Reporte eliminado exitosamente ID: {}", id);
    }

    /**
     * Actualizar reporte con validaciones
     */
    public ReporteDto update(Long id, ReporteDto reporteDto) {
        log.info("Actualizando reporte ID: {}", id);

        // Verificar que el reporte existe
        findById(id);

        // Validar datos de actualización
        validarReporte(reporteDto);

        // Actualizar
        ReporteDto reporteActualizado = reporteDAO.update(id, reporteDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar reporte"));

        log.info("reporte actualizado exitosamente ID: {}", id);
        return reporteActualizado;
    }



    /**
     * METODO PRIVADO: Validar datos de creación de un reporte
     */
    private void validarReporte(ReporteDto reporteDto) {
        // Validar ID de reporte (opcional en creación, pero no debe ser negativo)
        if (reporteDto.getIdReporte() != null && reporteDto.getIdReporte() <= 0) {
            throw new IllegalArgumentException("El idReporte, si se envía, debe ser un número positivo");
        }

        // Validar motivo del reporte
        if (reporteDto.getMotivo() == null) {
            throw new IllegalArgumentException("El motivo del reporte es obligatorio");
        }

        // Validar fecha del reporte
        if (reporteDto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del reporte es obligatoria");
        }
        if (reporteDto.getFecha().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del reporte no puede ser en el futuro");
        }

        // Validar cédula del usuario
        if (reporteDto.getCedula() == null || reporteDto.getCedula() <= 0) {
            throw new IllegalArgumentException("La cédula del usuario es obligatoria y debe ser un número positivo");
        }

        // Validar id de la reserva
        if (reporteDto.getIdReserva() == null || reporteDto.getIdReserva() <= 0) {
            throw new IllegalArgumentException("El id de la reserva es obligatorio y debe ser un número positivo");
        }

        if (reservaService.findById(reporteDto.getIdReserva()) == null ) {
            throw new IllegalArgumentException("El id de la reserva no existe");
        }

        if (usuarioService.findById(reporteDto.getCedula()) == null) {
            throw new IllegalArgumentException("El cedula no existe");
        }
    }

}
