package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.SalaService;
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
@RequestMapping("/qzwork_hub/salas")
@Tag(name = "Sala", description = "Controlador de salas")
@Slf4j
public class SalaController {
    private final SalaService salaService;

    @Autowired
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }
    /**
     * Obtener todos las salas
     */
    @GetMapping
    @Operation(
            summary = "Listar todos las salas",
            description = "Obtiene la lista completa de salas registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de sala obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SalaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrados"
            )
    })
    public ResponseEntity<List<SalaDto>> finAll() {
        log.debug("GET /qzwork_hub/salas - Obteniendo todos las salas");
        try {
            List<SalaDto> salas = salaService.findAll();
            log.debug("Se encontraron {} salas", salas.size());
            return ResponseEntity.ok(salas);
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener Sala por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar sala por ID",
            description = "Obtiene la información completa de una sala específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sala encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SalaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrado"
            )
    })
    public ResponseEntity<SalaDto> findById(
            @Parameter(description = "ID del sala", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/salas/{} - Buscando sala", id);

        try {
            SalaDto sala = salaService.findById(id);
            return ResponseEntity.ok(sala);
        } catch (RuntimeException e) {
            log.warn("Sala no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear un nuevo Sala
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo sala",
            description = "Crea un nuevo sala en el sistema con validación de email único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sala creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SalaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o email duplicado",
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
    public ResponseEntity<SalaDto> save(
            @Parameter(description = "Datos del sala a crear", required = true)
            @RequestBody SalaDto createDTO
    ) {
        log.info("POST /qzwork_hub/usuarios - Creando Salas: {}", createDTO.getIdSala());

        try {
            SalaDto salaCreada = salaService.save(createDTO);
            log.info("Salas creado exitosamente con ID: {}", salaCreada.getIdSala());
            return ResponseEntity.status(HttpStatus.CREATED).body(salaCreada);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear sala: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar sala existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar sala",
            description = "Actualiza la información de un sala existente. El email no se puede modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sala actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SalaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrado"
            )
    })
    public ResponseEntity<SalaDto> update(
            @Parameter(description = "ID del sala a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del sala", required = true)
            @RequestBody SalaDto updateDTO
    ) {
        log.info("PUT //qzwork_hub/salas/{} - Actualizando salas", id);

        try {
            SalaDto salaActualizado = salaService.update(id, updateDTO);
            log.info("Sala actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(salaActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Sala no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar sala ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar sala
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar sala",
            description = "Elimina un sala del sistema. No se puede eliminar si tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Sala eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: sala tiene reservas asociados"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del sala a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/salas/{} - Eliminando sala", id);

        try {
            salaService.deleteSala(id);
            log.info("Sala eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("No encontro")) {
                log.warn("Sala no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("reserva")) {
                log.warn("Intento de eliminar sala con reservas ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar sala ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
    @GetMapping("/buscar")
    @Operation(summary = "Buscar salas por filtros", description = "Busca salas por nombre, capacidad o ubicación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salas encontradas"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<Sala>> buscarSalas(
            @RequestParam(required = false) @Parameter(description = "Nombre de la sala") String nombre) {

        return salaService.findByNombre(nombre)
                .map(salas -> new ResponseEntity<>(salas, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/
}
