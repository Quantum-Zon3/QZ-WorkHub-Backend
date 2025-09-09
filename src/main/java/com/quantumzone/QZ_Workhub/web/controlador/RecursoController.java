package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoService;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
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
@RequestMapping("/qzwork_hub/recursos")
@Tag(name = "Recurso", description = "Controlador de recursos")
public class RecursoController {
    private final RecursoService recursoService;

    @Autowired
    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }


        @GetMapping
        @Operation(summary = "Obtener todos los recursos", description = "Devuelve una lista de todos los recursos registrados.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de recursos obtenida con éxito"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<List<Recurso>> getAllRecursos() {
            return new ResponseEntity<>(recursoService.findAll(), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener recurso por ID", description = "Devuelve un recurso específico basado en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<Recurso> getRecursoById(@PathVariable @Parameter(description = "ID del recurso") int id) {
            return recursoService.findById(id)
                    .map(recurso -> new ResponseEntity<>(recurso, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @PostMapping
        @Operation(summary = "Crear un nuevo recurso", description = "Crea un nuevo recurso con los datos proporcionados.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Recurso creado con éxito"),
                @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        public ResponseEntity<Recurso> createRecurso(@RequestBody @Parameter(description = "Datos del recurso a crear") Recurso recurso) {
            Recurso nuevoRecurso = recursoService.save(recurso);
            return new ResponseEntity<>(nuevoRecurso, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar un recurso", description = "Actualiza los datos de un recurso existente.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recurso actualizado con éxito"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<Recurso> updateRecurso(
                @PathVariable @Parameter(description = "ID del recurso") int id,
                @RequestBody @Parameter(description = "Datos actualizados del recurso") Recurso recurso) {
            return recursoService.update(id, recurso)
                    .map(updatedRecurso -> new ResponseEntity<>(updatedRecurso, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar un recurso", description = "Elimina un recurso basado en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Recurso eliminado con éxito"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<Void> deleteRecurso(@PathVariable @Parameter(description = "ID del recurso") int id) {
            boolean recursoEliminado = recursoService.deleteById(id);
            return recursoEliminado
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @GetMapping("/buscar")
        @Operation(summary = "Buscar recursos por filtros", description = "Busca recursos por nombre, tipo, estado o fecha de creación.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recursos encontrados"),
                @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        public ResponseEntity<List<Recurso>> buscarRecursos(
                @RequestParam(required = false) @Parameter(description = "Nombre del recurso") String nombre,
                @RequestParam(required = false) @Parameter(description = "Tipo de recurso") String tipo,
                @RequestParam(required = false) @Parameter(description = "Estado del recurso") String estado)
                 {

            return recursoService.findByFilters(nombre, tipo, estado)
                    .map(recursos -> new ResponseEntity<>(recursos, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
}
