package com.quantumzone.QZ_Workhub.web.controlador;
import java.util.List;
import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.PagoService;
//imports de anotacion springboot
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
public class PagoController {
    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Devuelve una lista de todos los pagos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PagoDto>> getAllPagos() {
        return new ResponseEntity<>(pagoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Devuelve un pago específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDto> getPagoById(@PathVariable @Parameter(description = "ID del pago") Long id) {
        try{
            PagoDto pago = pagoService.findById(id);
            return ResponseEntity.ok(pago);
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pago", description = "Crea un nuevo pago con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<PagoDto> createPago(@RequestBody @Parameter(description = "Datos del pago a crear") PagoDto pago) {
        PagoDto nuevoPago = pagoService.save(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago", description = "Actualiza los datos de un pago existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoDto> updatePago(
            @PathVariable @Parameter(description = "ID del pago") Long id,
            @RequestBody @Parameter(description = "Datos actualizados del pago") PagoDto pago) {
        PagoDto pagoActualizada = pagoService.update(id, pago);
        return new ResponseEntity<>(pagoActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago", description = "Elimina un pago basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> deletePago(@PathVariable @Parameter(description = "ID del pago") Long id) {
        try {
            pagoService.deletePago(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
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
