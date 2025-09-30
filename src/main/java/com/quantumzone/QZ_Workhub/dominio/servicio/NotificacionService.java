package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.NotificacionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class NotificacionService{

    private final NotificacionDAO notificacionDAO;

    @Autowired
    public NotificacionService(NotificacionDAO notificacionDAO) {
        this.notificacionDAO = notificacionDAO;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }
    private void initSampleData() {
    }

    /**
     * Crear una nueva notificacion con validaciones completas
     */

    public NotificacionDto save(NotificacionDto notificacionDto) {
        log.info("Creando una nueva notificacion: {}", notificacionDto.getMotivo());

        // Validar datos de la notificacion
        validarNotificacion(notificacionDto);

        // Crear notificacion
        NotificacionDto notificacionCreadda = notificacionDAO.save(notificacionDto);
        log.info("notificacion creado exitosamente con ID: {}", notificacionDto.getIdNotificacion());

        return notificacionCreadda;
    }
    /**
     * Buscar notificacion por ID
     */
    @Transactional(readOnly = true)
    public NotificacionDto findById(Long id) {
        log.debug("Buscando notificacion por ID: {}", id);

        return notificacionDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificacion no encontrada con ID: {}", id);
                    return new RuntimeException("Notificacion no encontrada con ID: " + id);
                });
    }

    /**
     * Obtener todos las notificaciones
     */
    @Transactional(readOnly = true)
    public List<NotificacionDto> findAll() {
        log.debug("Obteniendo todos las notificaciones: {}", notificacionDAO.findAll().size());
        return notificacionDAO.findAll();
    }

    /**
     * Eliminar notificacion
     */
    public void deleteNotificacion(Long id) {
        log.info("Eliminando notificacion ID: {}", id);

        // Verificar que el notificacion existe
        findById(id);

        // Eliminar notificacion
        boolean deleted = notificacionDAO.delete(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar notificacion con ID: " + id);
        }

        log.info("notificacion eliminado exitosamente ID: {}", id);
    }

    /**
     * Actualizar notificacion con validaciones
     */
    public NotificacionDto updateNotificacion(Long id, NotificacionDto notificacionDto) {
        log.info("Actualizando producto ID: {}", id);

        // Verificar que el notificacion existe
        findById(id);

        // Validar datos de actualización
        validarNotificacion(notificacionDto);

        // Actualizar
        NotificacionDto updatedProduct = notificacionDAO.update(id, notificacionDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar producto"));

        log.info("Producto actualizado exitosamente ID: {}", id);
        return updatedProduct;
    }
    /**
     * METODO PRIVADO: Validar datos de creación de una notificacion
     */
    private void validarNotificacion(NotificacionDto notificacionDto) {
        if (notificacionDto.getMotivo() == null || notificacionDto.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de la notificacion es obligatorio");
        }

        if (notificacionDto.getFecha() == null || notificacionDto.getFecha().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la notificación es obligatoria y debe ser válida");
        }


        if (notificacionDto.getDescripcion() == null || notificacionDto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion de la notificacion es obligatorio");
        }

        if (notificacionDto.getIdReserva() == null) {
            throw new IllegalArgumentException("El reserva es obligatorio");
        }

        // Validar longitud del motivo
        if (notificacionDto.getMotivo().length() > 45) {
            throw new IllegalArgumentException("El motivo no puede exceder 45 caracteres");
        }
        // Validar longitud del descripcion
        if (notificacionDto.getDescripcion().length() > 200) {
            throw new IllegalArgumentException("El motivo no puede exceder 200 caracteres");
        }
    }

}
