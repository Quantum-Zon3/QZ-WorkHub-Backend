package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
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
@RequestMapping("/qzwork_hub/reservas")
@Tag(name = "Reserva", description = "Controlador de reservas")
@Slf4j
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * Obtener todas las reservas
     */
    @GetMapping
    @Operation(
            summary = "Listar todas las reservas",
            description = "Obtiene la lista completa de reservas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reservas obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada"
            )
    })
    public ResponseEntity<List<ReservaDto>> findAll() {
        log.debug("GET /qzwork_hub/reservas - Obteniendo todas las reservas");
        try{
            List<ReservaDto> reservas = reservaService.findAll();
            log.debug("Se encontraron {} reservas", reservas.size());
            return ResponseEntity.ok(reservas);
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }

    }


    /**
     * Obtener reserva por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene la información completa de un reserva"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrado"
            )
    })
    public ResponseEntity<ReservaDto> findById(
            @Parameter(description = "ID del reserva", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/reservas/{} - Buscando reservas", id);

        try {
            ReservaDto pago = reservaService.findById(id);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            log.warn("reserva no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear una nueva reserva
     */
    @PostMapping
    @Operation(
            summary = "Crear nueva reserva",
            description = "Crea un nueva reserva"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDto.class)
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
    public ResponseEntity<ReservaDto> save(
            @Parameter(description = "Datos del reserva a crear", required = true)
            @RequestBody ReservaDto createDTO
    ) {
        log.info("POST /qzwork_hub/reservas - Creando reserva: {}", createDTO.getIdReserva());

        try {
            ReservaDto reservaSave = reservaService.save(createDTO);
            log.info("Reserva creada exitosamente con ID: {}", reservaSave.getIdReserva());
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaSave);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear reserva: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar reserva existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar reserva",
            description = "Actualiza la información de un reserva"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrado"
            )
    })
    public ResponseEntity<ReservaDto> update(
            @Parameter(description = "ID del reserva a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del reserva", required = true)
            @RequestBody ReservaDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/reservas/{} - Actualizando reserva", id);

        try {
            ReservaDto updatedReserva = reservaService.update(id, updateDTO);
            log.info("reserva actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedReserva);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Reserva no encontrada para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar reserva ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar reserva
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Reserva eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del reserva a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/reserva/{} - Eliminando reserva", id);

        try {
            reservaService.deleteReserva(id);
            log.info("Reserva eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Reserva no encontrado para eliminar ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    /*
    @GetMapping("/buscar")
    @Operation(summary = "Buscar reservas por cantidad de visitantes", description = "Busca reservas con cantidad de visitantes mayor o igual a un valor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservas")
    })
    public ResponseEntity<List<Reserva>> buscarReservas(
            @RequestParam(required = true) @Parameter(description = "Cantidad mínima de visitantes") Integer filtro) {

        return reservaService.findByCantidadVisitantes(filtro)
                .map(reservas -> new ResponseEntity<>(reservas, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/
}

