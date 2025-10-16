package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoService;
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
@Slf4j
public class RecursoController {
    private final RecursoService recursoService;

    @Autowired
    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }


    /**
     * Crear un nuevo recurso
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo recurso",
            description = "Crea un nuevo recurso en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "recurso creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDto.class)
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
    public ResponseEntity<RecursoDto> save(
            @Parameter(description = "Datos del recurso a crear", required = true)
            @RequestBody RecursoDto createDTO
    ) {
        log.info("POST /qzwork_hub/recursos - Creando recurso: {}", createDTO.getIdRecurso());

        try {
            RecursoDto createdRecurso = recursoService.save(createDTO);
            log.info("Recurso creado exitosamente con ID: {}", createdRecurso.getIdRecurso());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecurso);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear Recurso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener recurso por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar recurso por ID",
            description = "Obtiene la información completa de un recurso específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "recurso encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "recurso no encontrado"
            )
    })
    public ResponseEntity<RecursoDto> findById(
            @Parameter(description = "ID del recurso", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/recursos{} - Buscando recurso", id);

        try {
            RecursoDto recurso = recursoService.findById(id);
            return ResponseEntity.ok(recurso);
        } catch (RuntimeException e) {
            log.warn("Reccurso no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener todos los recursos
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los recursos",
            description = "Obtiene la lista completa de recursos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de recursos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )

    })
    public ResponseEntity<List<RecursoDto>> findAll() {
        log.debug("GET /qzwork_hub/recursos - Obteniendo todos los Recursos");
        try {
            List<RecursoDto> recursos = recursoService.findAll();
            log.debug("Se encontraron {} recursos", recursos.size());
            return ResponseEntity.ok(recursos);
        }catch (RuntimeException e) {
            return  ResponseEntity.notFound().build();
        }

    }

    /**
     * Actualizar recurso existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar recurso",
            description = "Actualiza la información de un recurso existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            )
    })
    public ResponseEntity<RecursoDto> update(
            @Parameter(description = "ID del recurso a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del recurso", required = true)
            @RequestBody RecursoDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/recursos/{} - Actualizando recurso", id);

        try {
            RecursoDto updatedRecurso = recursoService.update(id, updateDTO);
            log.info("Recurso actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedRecurso);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Recurso no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar recurso ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar recurso
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar recurso",
            description = "Elimina un recurso del sistema. No se puede eliminar si tiene reservas asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Recurso eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rendedor no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: recurso tiene reserva asociados"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del recurso a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /api/v1/sellers/{} - Eliminando recurso", id);

        try {
            recursoService.delete(id);
            log.info("recurso eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Recurso no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("reserva")) {
                log.warn("Intento de eliminar Recurso con reservas ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar recurso ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
