package com.quantumzone.QZ_Workhub.web.controlador;
import java.util.List;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.NotificacionService;
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
@Tag(name = "Notificacion", description = "Controlador de notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Devuelve una lista de todas las notificaciones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDto>> getAllNotificaciones() {
        return new ResponseEntity<>(notificacionService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID", description = "Devuelve una notificación específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDto> getNotificacionById(@PathVariable @Parameter(description = "ID de la notificación") Long id) {
        try {
            NotificacionDto noti = notificacionService.findById(id);
            return ResponseEntity.ok(noti);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva notificación", description = "Crea una nueva notificación con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<NotificacionDto> createNotificacion(@RequestBody @Parameter(description = "Datos de la notificación a crear") NotificacionDto notificacion) {
        NotificacionDto nuevaNotificacion = notificacionService.save(notificacion);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una notificación", description = "Actualiza los datos de una notificación existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDto> updateNotificacion(
            @PathVariable @Parameter(description = "ID de la notificación") Long id,
            @RequestBody @Parameter(description = "Datos actualizados de la notificación") NotificacionDto notificacion) {
        NotificacionDto notiActualizada = notificacionService.updateNotificacion(id, notificacion);
        return new ResponseEntity<>(notiActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificación", description = "Elimina una notificación basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        try {
            notificacionService.deleteNotificacion(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
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
