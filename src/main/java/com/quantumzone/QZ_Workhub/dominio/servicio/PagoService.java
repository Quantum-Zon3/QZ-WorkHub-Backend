package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.PagoDAO;
import com.quantumzone.QZ_Workhub.persistencia.dao.ReservaDAO;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de pago
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de pagoDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de pago
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class PagoService {

    private final PagoDAO pagoDAO;
    private final ReservaService reservaService;
    private final Clock clock;


    @Autowired
    public PagoService(PagoDAO pagoDAO, ReservaService reservaService, Clock clock) {
        this.pagoDAO = pagoDAO;
        this.reservaService = reservaService;
        this.clock = clock;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    /**
     * Crear un nuevo pago con validaciones de negocio
     */
    public PagoDto save(PagoDto pagoDto) {
        log.info("Creando nuevo pago con cedula: {}", pagoDto.getIdPago());
        // Validaciones adicionales de negocio
        validarPago(pagoDto);
        // Crear pago
        PagoDto pagoCreado = pagoDAO.save(pagoDto);
        log.info("pago creado exitosamente con ID: {}", pagoCreado.getIdPago());
        return pagoCreado;
    }

    /**
     * Buscar pago por ID
     */
    @Transactional(readOnly = true)
    public PagoDto findById(Long id) {
        log.info("Buscando pago por ID: {}", id);

        return pagoDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("pago no encontrado con ID: {}", id);
                    return new RuntimeException("pago no encontrado con ID: " + id);
                });
    }

    /**
     * Obtener todos las pago
     */
    @Transactional(readOnly = true)
    public List<PagoDto> findAll() {
        List<PagoDto> pagos = pagoDAO.findAll();
        log.info("Obteniendo todos los reporte: {}", pagos.size());
        return pagos;
    }

    /**
     * Eliminar pago
     */
    public void deletePago(Long id) {
        log.info("Eliminando pago ID: {}", id);

        // Verificar que el pago existe
        findById(id);

        // Eliminar pago
        boolean deleted = pagoDAO.delete(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar pago con ID: " + id);
        }

        log.info("pago eliminado exitosamente ID: {}", id);
    }

    /**
     * Actualizar pago con validaciones
     */
    public PagoDto update(Long id, PagoDto pagoDto) {
        log.info("Actualizando usuario ID: {}", id);

        // Verificar que el reporte existe
        findById(id);

        // Validar datos de actualización
        validarPago(pagoDto);

        // Actualizar
        PagoDto pagoActualizado = pagoDAO.update(id, pagoDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar pago"));

        log.info("pago actualizado exitosamente ID: {}", id);
        return pagoActualizado;
    }
    /**
     * MÉTODO PRIVADO: Validar datos de creación de un pago
     */
    private void validarPago(PagoDto pagoDto) {
        LocalDateTime ahora = LocalDateTime.now(clock);
        // Validar id del pago (opcional, pero si viene no puede ser negativo)
        if (pagoDto.getIdPago() != null && pagoDto.getIdPago() <= 0) {
            throw new IllegalArgumentException("El idPago, si se envía, debe ser un número positivo");
        }

        // Validar monto
        if (pagoDto.getMonto() == null) {
            throw new IllegalArgumentException("El monto del pago es obligatorio");
        }
        if (pagoDto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a 0");
        }

        // Validar fecha de realización
        if (pagoDto.getFechaRealizacion() == null) {
            throw new IllegalArgumentException("La fecha de realización es obligatoria");
        }
        if (pagoDto.getFechaRealizacion().isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("La fecha de realización no puede ser en el futuro");
        }

        // Validar metodo de pago
        if (pagoDto.getMetodoPago() == null) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }

        // Validar estado de pago
        if (pagoDto.getEstadoPago() == null) {
            throw new IllegalArgumentException("El estado del pago es obligatorio");
        }

        // Validar id de reserva
        if (pagoDto.getIdReserva() == null || pagoDto.getIdReserva() <= 0) {
            throw new IllegalArgumentException("El id de la reserva es obligatorio y debe ser un número positivo");
        }

        if (reservaService.findById(pagoDto.getIdReserva()) == null ) {
            throw new IllegalArgumentException("El id de la reserva no existe");
        }
        if (reservaService.findById(pagoDto.getIdReserva()).getIdReserva() == pagoDto.getIdReserva()) {
            throw new IllegalArgumentException("Esta Pago ya fue realizado para esa reserva");
        }
    }
}