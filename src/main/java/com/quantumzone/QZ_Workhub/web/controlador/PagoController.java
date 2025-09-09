package com.quantumzone.QZ_Workhub.web.controlador;
import java.time.LocalDate;
import java.util.List;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
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
    public ResponseEntity<List<Pago>> getAllPagos() {
        return new ResponseEntity<>(pagoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Devuelve un pago específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> getPagoById(@PathVariable @Parameter(description = "ID del pago") int id) {
        return pagoService.findById(id)
                .map(pago -> new ResponseEntity<>(pago, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pago", description = "Crea un nuevo pago con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Pago> createPago(@RequestBody @Parameter(description = "Datos del pago a crear") Pago pago) {
        Pago nuevoPago = pagoService.save(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago", description = "Actualiza los datos de un pago existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> updatePago(
            @PathVariable @Parameter(description = "ID del pago") int id,
            @RequestBody @Parameter(description = "Datos actualizados del pago") Pago pago) {
        return pagoService.update(id, pago)
                .map(updatedPago -> new ResponseEntity<>(updatedPago, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago", description = "Elimina un pago basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> deletePago(@PathVariable @Parameter(description = "ID del pago") int id) {
        boolean pagoEliminado = pagoService.deleteById(id);
        return pagoEliminado
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pagos por filtros", description = "Busca pagos por monto, fecha, usuario o método de pago.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<Pago>> buscarPagos(
            @RequestParam(required = false) @Parameter(description = "Monto del pago") Double monto,
            @RequestParam(required = false) @Parameter(description = "Fecha del pago") LocalDate fecha,
            @RequestParam(required = false) @Parameter(description = "Método de pago") String metodo,
            @RequestParam(required = false) @Parameter(description = "ID del usuario asociado") Integer usuarioId) {

        return pagoService.findByFilters(monto, fecha, metodo, usuarioId)
                .map(pagos -> new ResponseEntity<>(pagos, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
