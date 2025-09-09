package com.quantumzone.QZ_Workhub.web.controlador;
import java.time.LocalDate;
import java.util.List;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.persistencia.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RestController
    @RequestMapping("/notificaciones")
    public class NotificacionController {

        @GetMapping
        @Operation(summary = "Obtener todas las notificaciones", description = "Devuelve una lista de todas las notificaciones registradas.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida con éxito"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<List<Notificacion>> getAllNotificaciones() {
            return new ResponseEntity<>(notificacionService.findAll(), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener notificación por ID", description = "Devuelve una notificación específica basada en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
                @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
        })
        public ResponseEntity<Notificacion> getNotificacionById(@PathVariable @Parameter(description = "ID de la notificación") int id) {
            return notificacionService.findById(id)
                    .map(notificacion -> new ResponseEntity<>(notificacion, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @PostMapping
        @Operation(summary = "Crear una nueva notificación", description = "Crea una nueva notificación con los datos proporcionados.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Notificación creada con éxito"),
                @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        public ResponseEntity<Notificacion> createNotificacion(@RequestBody @Parameter(description = "Datos de la notificación a crear") Notificacion notificacion) {
            Notificacion nuevaNotificacion = notificacionService.save(notificacion);
            return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar una notificación", description = "Actualiza los datos de una notificación existente.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Notificación actualizada con éxito"),
                @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
        })
        public ResponseEntity<Notificacion> updateNotificacion(
                @PathVariable @Parameter(description = "ID de la notificación") int id,
                @RequestBody @Parameter(description = "Datos actualizados de la notificación") Notificacion notificacion) {
            return notificacionService.update(id, notificacion)
                    .map(updatedNotificacion -> new ResponseEntity<>(updatedNotificacion, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar una notificación", description = "Elimina una notificación basada en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Notificación eliminada con éxito"),
                @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
        })
        public ResponseEntity<Void> deleteNotificacion(@PathVariable @Parameter(description = "ID de la notificación") int id) {
            boolean notificacionEliminada = notificacionService.deleteById(id);
            return notificacionEliminada
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @GetMapping("/buscar")
        @Operation(summary = "Buscar notificaciones por filtros", description = "Busca notificaciones por título, mensaje, fecha o usuario.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Notificaciones encontradas"),
                @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        public ResponseEntity<List<Notificacion>> buscarNotificaciones(
                @RequestParam(required = false) @Parameter(description = "Título de la notificación") String titulo,
                @RequestParam(required = false) @Parameter(description = "Mensaje de la notificación") String mensaje,
                @RequestParam(required = false) @Parameter(description = "Fecha de la notificación") LocalDate fecha,
                @RequestParam(required = false) @Parameter(description = "ID del usuario asociado") Integer usuarioId) {

            return notificacionService.findByFilters(titulo, mensaje, fecha, usuarioId)
                    .map(notificaciones -> new ResponseEntity<>(notificaciones, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

}
