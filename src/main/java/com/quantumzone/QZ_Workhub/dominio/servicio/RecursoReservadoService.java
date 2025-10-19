package com.quantumzone.QZ_Workhub.dominio.servicio;

import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoReservadoDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de recurso reservado
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de SellerDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de vendedores
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */

@Service
@Transactional
@Slf4j
public class RecursoReservadoService {

    private final RecursoReservadoDAO recursoRDao;
    private final ReservaService reservaService;
    private final RecursoService recursoService;

    @Autowired
    public RecursoReservadoService(RecursoReservadoDAO recursoRDao, @Lazy ReservaService reservaService,@Lazy RecursoService recursoService) {
        this.recursoRDao = recursoRDao;
        this.reservaService = reservaService;
        this.recursoService = recursoService;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un recurso
    public RecursoReservadoDto save(RecursoReservadoDto recursoRDto) {
        log.info("Creando nuevo recurso reservado con id: {}", recursoRDto.getId());
        //Validaciones de negocio
        validarRecursoR(recursoRDto);

        //Creacion del recurso reservado
        RecursoReservadoDto recursoRCreado = recursoRDao.save(recursoRDto);
        log.info("Recurso reservado creado: {}", recursoRCreado.getId());
        return recursoRCreado;
    }

    // Encontrar un recurso reservado por id
    public RecursoReservadoDto findById(Long id) {
        log.info("Buscando recurso reservado con id: {}", id);

        return recursoRDao.findById(id).orElseThrow(() -> {
            log.warn("recurso reservado no encontrado con ID: {}", id);
            return new RuntimeException("recurso reservado no encontrado con ID: " + id);
        });
    }

    // Listar todos los recursos
    @Transactional(readOnly = true)
    public List<RecursoReservadoDto> findAll() {
        log.debug("Obteniendo todos los recursos reservado: {}", recursoRDao.findAll().size());
        return recursoRDao.findAll();
    }

    // Eliminar un recurso reservado por id
    public void delete(Long id) {
        log.info("Eliminando recurso reservado ID: {}", id);

        // Verificar que el recurso reservado existe
        findById(id);

        // Eliminar recurso reservado
        boolean deleted = recursoRDao.delete(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar recurso reservado con ID: " + id);
        }

        log.info("Recurso reservado eliminado exitosamente ID: {}", id);
    }

    public RecursoReservadoDto update(Long id, RecursoReservadoDto recursoRDto) {
        log.info("Actualizando recurso reservado ID: {}", id);

        // Verificar que el recurso reservado existe
        findById(id);

        // Validar datos de actualización
        validarRecursoR(recursoRDto);

        // Actualizar
        RecursoReservadoDto recursoRActualizado = recursoRDao.update(id, recursoRDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar recurso reservado"));

        log.info("recurso reservado actualizado exitosamente ID: {}", id);
        return recursoRActualizado;
    }


    private void validarRecursoR(RecursoReservadoDto recursoRDto) {

        // Validar id de recurso
        if (recursoRDto.getIdRecurso() == null || recursoRDto.getIdRecurso() == 0) {
            throw new IllegalArgumentException("El id del recurso solicitado es obligatorio");
        }

        RecursoDto recurso = recursoService.findById(recursoRDto.getIdRecurso());
        if (recurso == null) {
            throw new IllegalArgumentException("Recurso no encontrado");
        }

        // Validar id de reserva
        if (recursoRDto.getIdReserva() == null || recursoRDto.getIdReserva() == 0) {
            throw new IllegalArgumentException("El id de la reserva es obligatorio");
        }

        ReservaDto reserva = reservaService.findById(recursoRDto.getIdReserva());
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }

        // Validar la cantidad
        if (recursoRDto.getCantidad() == null || recursoRDto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad es obligatoria y debe ser mayor a cero");
        }

        // Validar fecha de inicio
        if (recursoRDto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        if (recursoRDto.getFechaInicio().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el futuro");
        }

        // Validar fecha de fin
        if (recursoRDto.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de finalización es obligatoria");
        }
        if (recursoRDto.getFechaFin().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser en el futuro");
        }

        // Validar que las fechas coincidan con la reserva
        if (!recursoRDto.getFechaInicio().equals(reserva.getFechaInicio()) ||
                !recursoRDto.getFechaFin().equals(reserva.getFechaFin())) {
            log.warn("Las fechas del recurso reservado no coinciden con las de la reserva");
            log.warn("Recurso - Inicio: {}, Fin: {}", recursoRDto.getFechaInicio(), recursoRDto.getFechaFin());
            log.warn("Reserva - Inicio: {}, Fin: {}", reserva.getFechaInicio(), reserva.getFechaFin());
            throw new IllegalArgumentException("Las fechas del recurso deben coincidir con las fechas de la reserva");
        }

        // VALIDACIÓN DE DISPONIBILIDAD: Verificar unidades disponibles en el rango de fechas
        validarDisponibilidadRecurso(recursoRDto, recurso);

        // Validar monto total
        if (recursoRDto.getMontoTotal() == null || recursoRDto.getMontoTotal() < 0) {
            throw new IllegalArgumentException("El monto total es obligatorio y debe ser mayor o igual a cero");
        }
    }

    /**
     * Valida que el recurso tenga unidades disponibles en el rango de fechas solicitado
     *
     * @param recursoRDto DTO con los datos de la reserva del recurso a validar
     * @param recurso Recurso que se desea reservar
     * @throws IllegalArgumentException si no hay suficientes unidades disponibles
     */
    private void validarDisponibilidadRecurso(RecursoReservadoDto recursoRDto, RecursoDto recurso) {

        // Obtener todas las reservas existentes del recurso
        List<RecursoReservadoDto> reservasExistentes = recursoRDao.findAll();

        // Filtrar las reservas que se solapan con el rango de fechas solicitado
        // y que son del mismo recurso (excluyendo la reserva actual si estamos actualizando)
        long unidadesReservadas = reservasExistentes.stream()
                .filter(rr -> rr.getIdRecurso().equals(recursoRDto.getIdRecurso()))
                .filter(rr -> {
                    // Excluir la reserva actual si estamos actualizando (si tiene ID)
                    if (recursoRDto.getId() != null && recursoRDto.getId().equals(rr.getId())) {
                        return false;
                    }
                    // Verificar si hay solapamiento de fechas
                    return haySolapamientoFechas(
                            recursoRDto.getFechaInicio(),
                            recursoRDto.getFechaFin(),
                            rr.getFechaInicio(),
                            rr.getFechaFin()
                    );
                })
                .mapToLong(RecursoReservadoDto::getCantidad)
                .sum();

        // Calcular unidades disponibles
        long unidadesDisponibles = recurso.getUnidades() - unidadesReservadas;

        log.debug("Validando disponibilidad - Recurso: {}, Unidades totales: {}, Unidades reservadas: {}, Unidades disponibles: {}, Cantidad solicitada: {}",
                recurso.getNombre(),
                recurso.getUnidades(),
                unidadesReservadas,
                unidadesDisponibles,
                recursoRDto.getCantidad()
        );

        // Validar que haya suficientes unidades disponibles
        if (recursoRDto.getCantidad() > unidadesDisponibles) {
            throw new IllegalArgumentException(
                    String.format("No hay suficientes unidades disponibles. Solicitadas: %d, Disponibles: %d en el rango de fechas [%s - %s]",
                            recursoRDto.getCantidad(),
                            unidadesDisponibles,
                            recursoRDto.getFechaInicio(),
                            recursoRDto.getFechaFin()
                    )
            );
        }
    }

    /**
     * Verifica si dos rangos de fechas se solapan
     *
     * @param inicio1 Fecha de inicio del primer rango
     * @param fin1 Fecha de fin del primer rango
     * @param inicio2 Fecha de inicio del segundo rango
     * @param fin2 Fecha de fin del segundo rango
     * @return true si hay solapamiento, false en caso contrario
     */
    private boolean haySolapamientoFechas(LocalDateTime inicio1, LocalDateTime fin1,
                                          LocalDateTime inicio2, LocalDateTime fin2) {
        // Hay solapamiento si:
        // - El inicio1 está entre inicio2 y fin2, O
        // - El fin1 está entre inicio2 y fin2, O
        // - El rango 1 contiene completamente al rango 2
        return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
    }
}

