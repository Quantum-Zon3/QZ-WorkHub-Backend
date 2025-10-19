package com.quantumzone.QZ_Workhub.dominio.servicio;
import java.time.Duration;
import java.time.LocalDateTime;

import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.ReservaDAO;

import com.quantumzone.QZ_Workhub.persistencia.mapper.SalaMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
/**
 * Implementación del servicio de reservas
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @RequiredArgsConstructor - Lombok genera constructor con dependencias final
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de reservaDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de reservas
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class ReservaService {

    private final ReservaDAO reservaDAO;
    private final RecursoReservadoService recursoReservadoService;
    private final ReporteService reporteService;
    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;
    private final SalaService salaService;


    @Autowired
    public ReservaService(ReservaDAO reservaDAO, @Lazy RecursoReservadoService recursoReservadoService, @Lazy ReporteService reporteService, @Lazy NotificacionService notificacionService, @Lazy UsuarioService usuarioService, @Lazy SalaService salaService ) {
        this.reservaDAO = reservaDAO;
        this.recursoReservadoService = recursoReservadoService;
        this.reporteService = reporteService;
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
        this.salaService = salaService;
        // Inicializamos algunos datos si es necesario
        initSampleData();

    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar una reserva
    public ReservaDto save(ReservaDto reservaDto) {

        log.info("Iniciando sala reserva: {}", reservaDto.getIdReserva());

        validarReserva(reservaDto);

        ReservaDto reservaCreada = reservaDAO.save(reservaDto);
        log.info("Sala reservada: {}", reservaCreada.getIdReserva());
        return reservaCreada;
    }

    /**
     * Buscar reserva por ID con manejo de errores
     */
    @Transactional(readOnly = true)
    public ReservaDto findById(Long idReserva) {
        log.debug("Buscando reserva por id: {}", idReserva);

        return reservaDAO.findById(idReserva)
                .orElseThrow(() -> {
                    log.warn("Reserva no encontrado: {}", idReserva);
                    return new RuntimeException("no encontrado la reserva por id: " + idReserva);
                });
    }

    /**
     * Obtener todas las salas
     */
    @Transactional(readOnly = true)
    public List<ReservaDto> findAll() {
        log.debug("Buscando reservas");
        return reservaDAO.findAll();
    }

    /**
     * Eliminar reservas con validaciones de negocio
     */
    public void deleteReserva(Long id) {
        log.info("Buscando reserva por id: {}", id);

        //verificar que la reserva si exista
        ReservaDto reservaBuscada = findById(id);

        //Regla de negocio: No eliminar si tiene reservas
        List<RecursoReservadoDto> recursoReservados = recursoReservadoService.findAll();
        for (RecursoReservadoDto recursoReservado : recursoReservados) {
            if (recursoReservado.getIdReserva().equals(id)) {
                log.warn("Intento de eliminar reserva con recurso. ID: {}, recurso: {}",recursoReservado.getIdReserva());
                throw new RuntimeException(
                        String.format("No se puede eliminar el recurso porque tiene reservas asociados")
                );
            }
        }

        //Regla de negocio: No eliminar si tiene reportes
        List<ReporteDto> reportes = reporteService.findAll();
        for (ReporteDto reporte : reportes) {
            if (reporte.getIdReserva().equals(id)) {
                log.warn("Intento de eliminar reserva con reportes. ID: {}, reportes: {}",reporte.getIdReserva());
                throw new RuntimeException(
                        String.format("No se puede eliminar el reserva porque tiene reportes asociados")
                );
            }
        }

        //Regla de negocio: No eliminar si tiene reportes
        List<NotificacionDto> notificaciones = notificacionService.findAll();
        for (NotificacionDto notificacionDto : notificaciones) {
            if (notificacionDto.getIdReserva().equals(id)) {
                log.warn("Intento de eliminar recurso con reserva. ID: {}, reserva: {}",notificacionDto.getIdReserva());
                throw new RuntimeException(
                        String.format("No se puede eliminar el recurso porque tiene notificaciones asociados")
                );
            }
        }

        //Eliminar reserva
        boolean eliminar = reservaDAO.delete(id);
        if (!eliminar) {
            throw new RuntimeException("No se logro eliminar la reserva co el ID: " + id);
        }

        log.info("Sala reservada: {}", reservaBuscada.getIdReserva() + "eliminada exitosamente");
    }

    /**
     * Actualizar reservq con validaciones
     */
    public ReservaDto update(Long id, ReservaDto reservaDto) {
        log.warn("Buscando reserva por id: {}", id);

        //verificamos si la reserva existe
        findById(id);

        //validaciones de negocio
        validarReserva(reservaDto);

        // Actualizar
        ReservaDto reservaActualizada = reservaDAO.update(id, reservaDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar la reserva con el ID: " + id));
        log.info("Reserva actualizada: {}",  reservaActualizada.getIdReserva() + "exitosamente");
        return reservaActualizada;
    }

    /**
     * Validar los datos para crear y/o actualizar la reserva
     */
    private void validarReserva(ReservaDto reservaDto) {
        // ===== VALIDAR FECHA DE INICIO =====
        if (reservaDto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }

// No permitir fechas pasadas
        if (reservaDto.getFechaInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede estar en el pasado.");
        }

// ===== VALIDAR FECHA DE FIN =====
        if (reservaDto.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de fin es obligatoria.");
        }

// No permitir fin antes del inicio
        if (reservaDto.getFechaFin().isBefore(reservaDto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

// No permitir reservas con duración cero (inicio == fin)
        if (reservaDto.getFechaFin().isEqual(reservaDto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser igual a la fecha de inicio.");
        }

// ===== VALIDACIONES DE LÓGICA DE NEGOCIO =====

// (1) Validar que la duración mínima de una reserva sea, por ejemplo, 30 minutos
        Duration duracion = Duration.between(reservaDto.getFechaInicio(), reservaDto.getFechaFin());
        if (duracion.toMinutes() < 30) {
            throw new IllegalArgumentException("La duración mínima de una reserva es de 30 minutos.");
        }

// (2) Validar que la duración máxima no exceda un límite (ejemplo: 8 horas)
        if (duracion.toHours() > 8) {
            throw new IllegalArgumentException("La duración máxima de una reserva es de 8 horas.");
        }

// (3) (Opcional) No permitir reservas fuera del horario de atención, por ejemplo 8:00 - 22:00
        LocalTime horaInicio = reservaDto.getFechaInicio().toLocalTime();
        LocalTime horaFin = reservaDto.getFechaFin().toLocalTime();

        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(22, 0);

        if (horaInicio.isBefore(apertura) || horaFin.isAfter(cierre)) {
            throw new IllegalArgumentException("Las reservas deben realizarse entre las 08:00 y las 22:00.");
        }

        // (4) (Opcional) Validar que no se crucen con otras reservas activas en la misma sala
        boolean hayConflicto = reservaDAO.existeConflicto(
                reservaDto.getIdSala(),
                reservaDto.getFechaInicio(),
                reservaDto.getFechaFin()
        );
        if (hayConflicto) {
            throw new IllegalArgumentException("Ya existe una reserva en ese horario para la sala seleccionada.");
        }

        if(reservaDto.getCantidadVisitantes() > salaService.findById(reservaDto.getIdReserva()).getCapacidad()){
            throw new IllegalArgumentException("La reserva excede la cantidad de visitantes para la sala seleccionada.");
        }

        //validar monto total
        if (reservaDto.getMontoTotal() == null || reservaDto.getMontoTotal() <= 0) {
            throw new IllegalArgumentException("El monto total es obligatorio y no puede ser ni 0 ni negativo");
        }

        //validar cantidad de personas
        if (reservaDto.getCantidadVisitantes() == null || reservaDto.getCantidadVisitantes() <= 0) {
            throw new IllegalArgumentException("La cantidad de visitantes es obligatoria y no puede ser ni 0 ni negativa");
        }
        //validar cedula
        if (reservaDto.getCedula() == null || reservaDto.getCedula() <= 0) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }

        if(usuarioService.findById(reservaDto.getCedula()) == null){
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        //validar id sala
        if (reservaDto.getIdSala() == null || reservaDto.getIdSala() <= 0) {
            throw new IllegalArgumentException("El ID de la sala es obligatorio");
        }

        if (salaService.findById(reservaDto.getIdSala()) == null) {
            throw new IllegalArgumentException("sala no encontrada");
        }

        /*
        //validar id pago
        if (reservaDto.getIdPago() == null || reservaDto.getIdPago() <= 0) {
            throw new IllegalArgumentException("El ID del pago es obligatorio");
        }
        */

    }

    /**
     * Validar los datos para actualizar una reserva existente
     */
    private void validarReservaParaUpdate(ReservaDto reservaDto) {
        // ===== VALIDAR ID DE RESERVA =====
        if (reservaDto.getIdReserva() == null || reservaDto.getIdReserva() <= 0) {
            throw new IllegalArgumentException("El ID de la reserva es obligatorio para actualizar.");
        }

        // ===== VALIDAR FECHAS =====
        if (reservaDto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }

        if (reservaDto.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de fin es obligatoria.");
        }

        // No permitir fin antes del inicio
        if (reservaDto.getFechaFin().isBefore(reservaDto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // No permitir duración cero
        if (reservaDto.getFechaFin().isEqual(reservaDto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser igual a la fecha de inicio.");
        }

        // Si las fechas fueron modificadas, validar que no estén en el pasado
        if (reservaDto.getFechaInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede estar en el pasado.");
        }

        // ===== VALIDAR DURACIÓN =====
        Duration duracion = Duration.between(reservaDto.getFechaInicio(), reservaDto.getFechaFin());
        if (duracion.toMinutes() < 30) {
            throw new IllegalArgumentException("La duración mínima de una reserva es de 30 minutos.");
        }
        if (duracion.toHours() > 8) {
            throw new IllegalArgumentException("La duración máxima de una reserva es de 8 horas.");
        }

        // ===== VALIDAR HORARIO DE ATENCIÓN =====
        LocalTime horaInicio = reservaDto.getFechaInicio().toLocalTime();
        LocalTime horaFin = reservaDto.getFechaFin().toLocalTime();
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(22, 0);

        if (horaInicio.isBefore(apertura) || horaFin.isAfter(cierre)) {
            throw new IllegalArgumentException("Las reservas deben realizarse entre las 08:00 y las 22:00.");
        }

        // ===== VALIDAR SALA =====
        if (reservaDto.getIdSala() == null || reservaDto.getIdSala() <= 0) {
            throw new IllegalArgumentException("El ID de la sala es obligatorio.");
        }

        var sala = salaService.findById(reservaDto.getIdSala());
        if (sala == null) {
            throw new IllegalArgumentException("No se encontró la sala seleccionada.");
        }

        // Validar que la cantidad de visitantes no exceda la capacidad
        if (reservaDto.getCantidadVisitantes() > sala.getCapacidad()) {
            throw new IllegalArgumentException("La reserva excede la capacidad máxima de la sala seleccionada.");
        }

        // ===== VALIDAR USUARIO =====
        if (reservaDto.getCedula() == null || reservaDto.getCedula() <= 0) {
            throw new IllegalArgumentException("La cédula del usuario es obligatoria.");
        }

        if (usuarioService.findById(reservaDto.getCedula()) == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // ===== VALIDAR MONTO =====
        if (reservaDto.getMontoTotal() == null || reservaDto.getMontoTotal() <= 0) {
            throw new IllegalArgumentException("El monto total es obligatorio y debe ser mayor que cero.");
        }

        // ===== VALIDAR CANTIDAD DE VISITANTES =====
        if (reservaDto.getCantidadVisitantes() == null || reservaDto.getCantidadVisitantes() <= 0) {
            throw new IllegalArgumentException("La cantidad de visitantes es obligatoria y debe ser mayor que cero.");
        }

        // ===== VALIDAR CONFLICTO CON OTRAS RESERVAS =====
        boolean hayConflicto = reservaDAO.existeConflictoExcluyendoActual(
                reservaDto.getIdReserva(), // 👈 excluye la reserva que se está actualizando
                reservaDto.getIdSala(),
                reservaDto.getFechaInicio(),
                reservaDto.getFechaFin()

        );

        if (hayConflicto) {
            throw new IllegalArgumentException("Ya existe una reserva en ese horario para la sala seleccionada.");
        }
    }


}

