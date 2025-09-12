package com.quantumzone.QZ_Workhub.web.controlador;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoReservadoService;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
//imports de anotacion springboot
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
import java.util.List;
@RestController
@RequestMapping("/qzwork_hub/recursosReservados")
@Tag(name = "RecursoReservado", description = "Controlador de recursos reservados")
public class RecursoReservadoController {

    private final RecursoReservadoService recursoReservadoService;

    @Autowired
    public RecursoReservadoController(RecursoReservadoService recursoReservadoService) {
        this.recursoReservadoService = recursoReservadoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los recursos reservados", description = "Devuelve una lista de todos los recursos reservados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de recursos reservados obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<RecursoReservado>> getAllRecursosReservados() {
        return new ResponseEntity<>(recursoReservadoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener recurso reservado por ID", description = "Devuelve un recurso reservado específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso reservado encontrado"),
            @ApiResponse(responseCode = "404", description = "Recurso reservado no encontrado")
    })
    public ResponseEntity<RecursoReservado> getRecursoReservadoById(
            @PathVariable @Parameter(description = "ID del recurso reservado") Long id) {
        return recursoReservadoService.findById(id)
                .map(reserva -> new ResponseEntity<>(reserva, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un recurso reservado", description = "Crea una nueva reserva de recurso con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recurso reservado creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<RecursoReservado> createRecursoReservado(
            @RequestBody @Parameter(description = "Datos del recurso reservado a crear") RecursoReservado recursoReservado) {
        RecursoReservado nuevoRecurso = recursoReservadoService.save(recursoReservado);
        return new ResponseEntity<>(nuevoRecurso, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Actualizar un recurso reservado", description = "Actualiza los datos de una reserva de recurso existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso reservado actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<RecursoReservado> updateRecursoReservado(
            @RequestBody @Parameter(description = "Datos actualizados del recurso reservado") RecursoReservado recursoReservado) {
        RecursoReservado updatedRecurso = recursoReservadoService.update(recursoReservado);
        return new ResponseEntity<>(updatedRecurso, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un recurso reservado", description = "Elimina una reserva de recurso basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recurso reservado eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Recurso reservado no encontrado")
    })
    public ResponseEntity<Void> deleteRecursoReservado(
            @PathVariable @Parameter(description = "ID del recurso reservado") Long id) {
        recursoReservadoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

/*
        @GetMapping("/buscar")
        @Operation(summary = "Buscar recursos reservados por filtros", description = "Busca recursos reservados por recurso, usuario o rango de fechas.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recursos reservados encontrados"),
                @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        public ResponseEntity<List<RecursoReservado>> buscarRecursosReservados(
                @RequestParam(required = false) @Parameter(description = "ID del recurso") Integer recursoId,
                @RequestParam(required = false) @Parameter(description = "ID del usuario") Integer usuarioId) {

            return recursoReservadoService.findByFilters(recursoId, usuarioId)
                    .map(reservas -> new ResponseEntity<>(reservas, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

 */
}

