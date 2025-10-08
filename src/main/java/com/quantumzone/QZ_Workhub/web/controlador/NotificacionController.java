package com.quantumzone.QZ_Workhub.web.controlador;
import java.util.List;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.NotificacionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//imports para documentar swagen
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/qzwork_hub/notificaciones")
@Slf4j
@Tag(name = "Notificacion", description = "Controlador de notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Obtener todas las Notificaciones
     */
    @GetMapping
    @Operation(
            summary = "Listar todas las notificacion",
            description = "Obtiene la lista completa de productos con información del notificacion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de notificacion obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificacionDto.class)
                    )
            )
    })
    public ResponseEntity<List<NotificacionDto>> findAll() {
        log.debug("GET /qzwork_hub/notificaciones - Obteniendo todos los notificacion");

        List<NotificacionDto> notificaciones = notificacionService.findAll();
        log.debug("Se encontraron {} notificaciones", notificaciones.size());
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificacion por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar notificacion por ID",
            description = "Obtiene la información completa de un notificacion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "notificacion encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificacionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "notificacion no encontrado"
            )
    })
    public ResponseEntity<NotificacionDto> findById(
            @Parameter(description = "ID del notificacion", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/notificaciones{} - Buscando notificacion", id);

        try {
            NotificacionDto notificacion = notificacionService.findById(id);
            return ResponseEntity.ok(notificacion);
        } catch (RuntimeException e) {
            log.warn("notificacion no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear una nueva notificacion
     */
    @PostMapping
    @Operation(
            summary = "Crear nueva notificacion",
            description = "Crea un nueva notificacion asociado a una reserva existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "notificacion creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificacionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<NotificacionDto> save(
            @Parameter(description = "Datos de la notificacion a crear", required = true)
            @RequestBody NotificacionDto createDTO
    ) {
        log.info("POST /qzwork_hub/notificaciones - Creando notificacion: {}", createDTO.getDescripcion());

        try {
            NotificacionDto notificacionSave = notificacionService.save(createDTO);
            log.info("notificacion creado exitosamente con ID: {}", notificacionSave.getIdNotificacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacionSave);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear notificacion: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * Eliminar notificacion
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar notificacion",
            description = "Elimina una notificacion del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Notificacion eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificacion no encontrada"
            )
    })
    public ResponseEntity<Void> deleteNotificacion(
            @Parameter(description = "ID del notificacion a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/notificaciones{} - Eliminando notificaion", id);

        try {
            notificacionService.deleteNotificacion(id);
            log.info("Notificacion eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Notificacion no encontrado para eliminar ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualizar notificacion existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar notificacion",
            description = "Actualiza la información de un notificacion. No se puede cambiar la reserva."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "notificacion actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificacionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "notifiacion no encontrado"
            )
    })
    public ResponseEntity<NotificacionDto> updateNotificacion(
            @Parameter(description = "ID del notifiacion a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del notificacion", required = true)
            @RequestBody NotificacionDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/notificaciones{} - Actualizando notificacion", id);

        try {
            NotificacionDto updatedProduct = notificacionService.updateNotificacion(id, updateDTO);
            log.info("notifiacion actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("notificacion no encontrada para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar notificacion ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

        /*
        @GetMapping("/buscar")
        @Operation(summary = "Buscar notificaciones por filtros", description = "Busca notificaciones por título, mensaje, fecha o usuario.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Notificaciones encontradas"),
                @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        public ResponseEntity<List<Notificacion>> buscarNotificaciones(
                @RequestParam(required = false) @Parameter(description = "Reserva completa") Reserva reserva,
                @RequestParam(required = false) @Parameter(description = "La cedula del usuario") Long cedula) {
        if(reserva!=null) {
            return notificacionService.findByReserva(reserva)
                    .map(notificaciones -> new ResponseEntity<>(notificaciones, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
            return notificacionService.findByReserva(cedula)
                    .map(notificaciones -> new ResponseEntity<>(notificaciones, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        }*/
}
