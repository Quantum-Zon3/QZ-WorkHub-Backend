package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
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

import java.time.LocalDate;
import java.util.List;@RestController
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
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return new ResponseEntity<>(reservaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Devuelve una reserva específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Reserva> getReservaById(
            @PathVariable @Parameter(description = "ID de la reserva") int id) {
        return reservaService.findById(id)
                .map(reserva -> new ResponseEntity<>(reserva, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Reserva> createReserva(
            @RequestBody @Parameter(description = "Datos de la reserva a crear") Reserva reserva) {
        Reserva nuevaReserva = reservaService.save(reserva);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva", description = "Actualiza los datos de una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Reserva> updateReserva(
            @PathVariable @Parameter(description = "ID de la reserva") int id,
            @RequestBody @Parameter(description = "Datos actualizados de la reserva") Reserva reserva) {
        return reservaService.update(id, reserva)
                .map(updatedReserva -> new ResponseEntity<>(updatedReserva, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> deleteReserva(
            @PathVariable @Parameter(description = "ID de la reserva") int id) {
        boolean eliminada = reservaService.deleteById(id);
        return eliminada
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar reservas por filtros", description = "Busca reservas por usuario, recurso, estado o fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<Reserva>> buscarReservas(
            @RequestParam(required = false) @Parameter(description = "ID del usuario asociado") Integer usuarioId,
            @RequestParam(required = false) @Parameter(description = "ID del recurso asociado") Integer recursoId,
            @RequestParam(required = false) @Parameter(description = "Estado de la reserva") String estado,
            @RequestParam(required = false) @Parameter(description = "Fecha de la reserva") LocalDate fecha) {

        return reservaService.findByFilters(usuarioId, recursoId, estado, fecha)
                .map(reservas -> new ResponseEntity<>(reservas, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
