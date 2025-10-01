package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
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
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Devuelve una lista de todas las reservas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReservaDto>> getAllReservas() {
        return new ResponseEntity<>(reservaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Devuelve una reserva específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDto> getReservaById(
            @PathVariable @Parameter(description = "ID de la reserva") Long id) {
        try{
            ReservaDto reserva = reservaService.findById(id);
            return ResponseEntity.ok(reserva);
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ReservaDto> createReserva(
            @RequestBody @Parameter(description = "Datos de la reserva a crear") ReservaDto reserva) {
        ReservaDto nuevaReserva = reservaService.save(reserva);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Actualizar una reserva", description = "Actualiza los datos de una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ReservaDto> updateReserva(
            @RequestBody @Parameter(description = "id de la reserva") Long id,
            @RequestBody @Parameter(description = "Datos actualizados de la reserva") ReservaDto reserva) {
        ReservaDto updatedReserva = reservaService.update(id, reserva);
        return new ResponseEntity<>(updatedReserva, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> deleteReserva(
            @PathVariable @Parameter(description = "ID de la reserva") Long id) {
        reservaService.deleteReserva(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

