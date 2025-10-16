package com.quantumzone.QZ_Workhub.web.controlador;
import java.util.List;
import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.PagoService;
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

@RestController
@RequestMapping("/qzwork_hub/pagos")
@Tag(name = "Pago", description = "Controlador de pagos")
@Slf4j
public class PagoController {
    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    /**
     * Obtener todas los pagos
     */
    @GetMapping
    @Operation(
            summary = "Listar todas los pagos",
            description = "Obtiene la lista completa de pagos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pagos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pagos no encontrado"
            )
    })
    public ResponseEntity<List<PagoDto>> findAll() {
        log.debug("GET /qzwork_hub/pagos - Obteniendo todos los pagos");
        try{
            List<PagoDto> pagos = pagoService.findAll();
            log.debug("Se encontraron {} notificaciones", pagos.size());
            return ResponseEntity.ok(pagos);
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Obtener pago por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene la información completa de un pago"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "pago encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "pago no encontrado"
            )
    })
    public ResponseEntity<PagoDto> findById(
            @Parameter(description = "ID del pago", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/pagos/{} - Buscando pago", id);

        try {
            PagoDto pago = pagoService.findById(id);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            log.warn("pago no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear un nuevo pago
     */
    @PostMapping
    @Operation(
            summary = "Crear nueva notificacion",
            description = "Crea un nuevo pago asociado a una reserva existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pago creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoDto.class)
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
    public ResponseEntity<PagoDto> save(
            @Parameter(description = "Datos del pago a crear", required = true)
            @RequestBody PagoDto createDTO
    ) {
        log.info("POST /qzwork_hub/pagos - Creando pago: {}", createDTO.getIdPago());

        try {
            PagoDto pagoSave = pagoService.save(createDTO);
            log.info("Pago creado exitosamente con ID: {}", pagoSave.getIdPago());
            return ResponseEntity.status(HttpStatus.CREATED).body(pagoSave);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear pago: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar pago existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar pago",
            description = "Actualiza la información de un pago. No se puede cambiar la reserva."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "pago actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado"
            )
    })
    public ResponseEntity<PagoDto> update(
            @Parameter(description = "ID del pago a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del pago", required = true)
            @RequestBody PagoDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/pagos/{} - Actualizando pago", id);

        try {
            PagoDto updatedPago = pagoService.update(id, updateDTO);
            log.info("pago actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedPago);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Pago no encontrada para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar pago ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar pago
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar pago",
            description = "Elimina una pago del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pago eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrada"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del pago a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/pago/{} - Eliminando pago", id);

        try {
            pagoService.deletePago(id);
            log.info("Pago eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Pago no encontrado para eliminar ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    /*
    @GetMapping("/buscar")
    @Operation(summary = "Buscar pagos por filtros", description = "Busca pagos por monto, fecha, usuario o método de pago.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<Pago>> buscarPagos(
            @RequestParam(required = false) @Parameter(description = "Reserva completa") Reserva reserva,
            @RequestParam(required = false) @Parameter(description = "Cedula del usuario asociado con la reserva") Long cedula) {
        if(reserva != null) {
            return pagoService.findByReserva(reserva)
                    .map(notificaciones -> new ResponseEntity<>(notificaciones, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        }
            return pagoService.findByReservaUsuarioCedula(cedula)
                    .map(notificaciones -> new ResponseEntity<>(notificaciones, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    */
}
