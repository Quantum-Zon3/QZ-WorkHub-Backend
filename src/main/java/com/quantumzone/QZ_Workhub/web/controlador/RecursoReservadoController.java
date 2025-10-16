package com.quantumzone.QZ_Workhub.web.controlador;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoReservadoService;
//imports de anotacion springboot
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
import java.util.List;

@RestController
@RequestMapping("/qzwork_hub/recursosReservados")
@Tag(name = "RecursoReservado", description = "Controlador de recursos reservados")
@Slf4j
public class RecursoReservadoController {

    private final RecursoReservadoService recursoReservadoService;

    @Autowired
    public RecursoReservadoController(RecursoReservadoService recursoReservadoService) {
        this.recursoReservadoService = recursoReservadoService;
    }

    /**
     * Crear un nuevo recurso reservado
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo recurso reservado",
            description = "Crea un nuevo recurso reservado en el sistema "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso reservado creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoReservadoDto.class)
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
    public ResponseEntity<RecursoReservadoDto> save(
            @Parameter(description = "Datos del recurso reservado a crear", required = true)
            @RequestBody RecursoReservadoDto createDTO
    ) {
        log.info("POST /qzwork_hub/recursosReservados - Creando recurso reservado: {}", createDTO.toString());

        try {
            RecursoReservadoDto recursoReservadoCreado = recursoReservadoService.save(createDTO);
            log.info("recurso reservado creado exitosamente con ID: {}", recursoReservadoCreado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(recursoReservadoCreado);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear recurso reservado: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener todos los recurso reservado
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los recurso reservado",
            description = "Obtiene la lista completa de recurso reservado registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de recurso reservado obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoReservadoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "recurso reservado no encontrado"
            )
    })
    public ResponseEntity<List<RecursoReservadoDto>> finAll() {
        log.debug("GET /qzwork_hub/recursosReservados - Obteniendo todos los recurso reservado");
        try {
            List<RecursoReservadoDto> recursoReser = recursoReservadoService.findAll();
            log.debug("Se encontraron {} recurso reservado", recursoReser.size());
            return ResponseEntity.ok(recursoReser);
        }catch (EmptyResultDataAccessException e){
            log.error("No se encontro el recurso reservado exitosamente");
            return ResponseEntity.notFound().build();
        }

    }


    /**
     * Obtener recurso reservado por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar recurso reservado por ID",
            description = "Obtiene la información completa de un usuario específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso reservado encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoReservadoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "recurso reservado no encontrado"
            )
    })
    public ResponseEntity<RecursoReservadoDto> findById(
            @Parameter(description = "ID del recurso reservado", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/recursosReservados/{} - Buscando recurso reservado", id);

        try {
            RecursoReservadoDto recursoReserv = recursoReservadoService.findById(id);
            return ResponseEntity.ok(recursoReserv);
        } catch (RuntimeException e) {
            log.warn("Recurso reservado no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Actualizar recurso reservado existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usaurio",
            description = "Actualiza la información de un usuario existente. El email no se puede modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecursoReservadoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "recurso reservado no encontrado"
            )
    })
    public ResponseEntity<RecursoReservadoDto> update(
            @Parameter(description = "ID del recurso reservado a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del recurso reservado", required = true)
            @RequestBody RecursoReservadoDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/recursosReservados/{} - Actualizando recurso reservado", id);

        try {
            RecursoReservadoDto recursoReservadoActualizado = recursoReservadoService.update(id, updateDTO);
            log.info("recurso reservado actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(recursoReservadoActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("recurso reservado no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar recurso reservado ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar recurso reservado
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar recurso reservado",
            description = "Elimina un recurso reservado del sistema. No se puede eliminar si tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "recurso reservado eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "recurso reservado no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: usuario tiene reservas asociados"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del recurso reservado a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/recursoReservado{} - Eliminando recurso reservado", id);

        try {
            recursoReservadoService.delete(id);
            log.info("recurso reservado eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Recurso reservado no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("reserva")) {
                log.warn("Intento de recurso reservado con reservas ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar recurso reservado ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

