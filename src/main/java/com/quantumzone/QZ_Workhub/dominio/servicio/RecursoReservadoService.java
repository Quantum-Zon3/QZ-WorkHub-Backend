package com.quantumzone.QZ_Workhub.dominio.servicio;

import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoReservadoDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
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
    private final Clock clock;

    @Autowired
    public RecursoReservadoService(RecursoReservadoDAO recursoRDao, @Lazy ReservaService reservaService,@Lazy RecursoService recursoService, Clock clock) {
        this.recursoRDao = recursoRDao;
        this.reservaService = reservaService;
        this.recursoService = recursoService;
        this.clock = clock;
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
            log.warn("recurso reservado no encontrada con ID: {}", id);
            return new RuntimeException("recurso reservado no encontrada con ID: " + id);
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
        LocalDateTime now = LocalDateTime.now(clock);
        // Validar id de recurso
        if (recursoRDto.getIdRecurso() == null || recursoRDto.getIdRecurso() == 0) {
            throw new IllegalArgumentException("El id del recurso solicitado es obligatorio");
        }

        if (recursoService.findById(recursoRDto.getIdRecurso()) == null) {
            throw new IllegalArgumentException("Recurso no encontrado");
        }

        // Validar id de reserva
        if (recursoRDto.getIdReserva() == null || recursoRDto.getIdReserva() == 0) {
            throw   new IllegalArgumentException("El id de la reserva es obligatio");
        }

        if(reservaService.findById(recursoRDto.getIdReserva()).getIdReserva() == null) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }


        // Validar la cantidad
        if (recursoRDto.getCantidad() == null || recursoRDto.getCantidad() == 0) {
            throw new IllegalArgumentException("El cantidad es obligatorio");
        }

        // Validar fecha de inicio
        if (recursoRDto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        if (recursoRDto.getFechaInicio().isAfter(now)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el futuro");
        }

        // Validar fecha de fin
        if (recursoRDto.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de finalizacion es obligatoria");
        }
        if (recursoRDto.getFechaFin().isAfter(now)) {
            throw new IllegalArgumentException("La fecha de finalizacion no puede ser en el futuro");
        }

        //Validar monto total
        if(recursoRDto.getMontoTotal() == null || recursoRDto.getMontoTotal()<0) {
            throw new IllegalArgumentException("La monto total es obligatorio y debe ser mayor a cero");
        }
    }
}

