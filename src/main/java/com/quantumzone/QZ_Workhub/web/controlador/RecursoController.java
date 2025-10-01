package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoService;
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
        public ResponseEntity<List<RecursoDto>> getAllRecursos() {
            return new ResponseEntity<>(recursoService.findAll(), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener recurso por ID", description = "Devuelve un recurso específico basado en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<RecursoDto> getRecursoById(@PathVariable @Parameter(description = "ID del recurso") Long id) {
            try {
                RecursoDto recursoDto = recursoService.findById(id);
                return ResponseEntity.ok(recursoDto);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }

        @PostMapping
        @Operation(summary = "Crear un nuevo recurso", description = "Crea un nuevo recurso con los datos proporcionados.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Recurso creado con éxito"),
                @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        public ResponseEntity<RecursoDto> createRecurso(@RequestBody @Parameter(description = "Datos del recurso a crear") RecursoDto recurso) {
            RecursoDto nuevoRecurso = recursoService.save(recurso);
            return new ResponseEntity<>(nuevoRecurso, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar un recurso", description = "Actualiza los datos de un recurso existente.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recurso actualizado con éxito"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<RecursoDto> updateRecurso(
                @PathVariable @Parameter(description = "ID del recurso") Long id,
                @RequestBody @Parameter(description = "Datos actualizados del recurso") RecursoDto recurso) {
            RecursoDto recursoActualizado = recursoService.update(id, recurso);
            return new ResponseEntity<>(recursoActualizado, HttpStatus.OK);

        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar un recurso", description = "Elimina un recurso basado en su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Recurso eliminado con éxito"),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
        })
        public ResponseEntity<Void> deleteRecurso(@PathVariable @Parameter(description = "ID del recurso") Long id) {
            try {
                recursoService.delete(id);
                return ResponseEntity.noContent().build();
            } catch (EmptyResultDataAccessException e) {
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("/buscar")
        @Operation(summary = "Buscar recursos por filtros", description = "Busca recursos por nombre, tipo, estado o fecha de creación.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Recursos encontrados"),
                @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        public ResponseEntity<List<RecursoDto>> buscarRecursosPorTipo(
                @RequestParam(required = false) @Parameter(description = "Tipo de recurso") TipoRecurso tipo){
                try {
                    List<RecursoDto> recursos = recursoService.findByTipoRecurso(tipo);
                    return ResponseEntity.ok(recursos);
                } catch (RuntimeException e) {
                    return ResponseEntity.notFound().build();
                }
        }
}
