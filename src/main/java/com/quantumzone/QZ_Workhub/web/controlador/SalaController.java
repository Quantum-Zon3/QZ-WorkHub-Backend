package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.servicio.SalaService;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
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
import java.util.List;
@RestController
@RequestMapping("/qzwork_hub/salas")
@Tag(name = "Sala", description = "Controlador de salas")
public class SalaController {
    private final SalaService salaService;

    @Autowired
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }
    @GetMapping
    @Operation(summary = "Obtener todas las salas", description = "Devuelve una lista de todas las salas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de salas obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Sala>> getAllSalas() {
        return new ResponseEntity<>(salaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sala por ID", description = "Devuelve una sala específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala encontrada"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada")
    })
    public ResponseEntity<Sala> getSalaById(
            @PathVariable @Parameter(description = "ID de la sala") int id) {
        return salaService.findById(id)
                .map(sala -> new ResponseEntity<>(sala, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sala", description = "Crea una nueva sala con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sala creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Sala> createSala(
            @RequestBody @Parameter(description = "Datos de la sala a crear") Sala sala) {
        Sala nuevaSala = salaService.save(sala);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sala", description = "Actualiza los datos de una sala existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada")
    })
    public ResponseEntity<Sala> updateSala(
            @PathVariable @Parameter(description = "ID de la sala") int id,
            @RequestBody @Parameter(description = "Datos actualizados de la sala") Sala sala) {
        return salaService.update(id, sala)
                .map(updatedSala -> new ResponseEntity<>(updatedSala, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sala", description = "Elimina una sala basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sala eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada")
    })
    public ResponseEntity<Void> deleteSala(
            @PathVariable @Parameter(description = "ID de la sala") int id) {
        boolean eliminada = salaService.deleteById(id);
        return eliminada
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar salas por filtros", description = "Busca salas por nombre, capacidad o ubicación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salas encontradas"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<Sala>> buscarSalas(
            @RequestParam(required = false) @Parameter(description = "Nombre de la sala") String nombre,
            @RequestParam(required = false) @Parameter(description = "Capacidad mínima de la sala") Integer capacidad,
            @RequestParam(required = false) @Parameter(description = "Ubicación de la sala") String ubicacion) {

        return salaService.findByFilters(nombre, capacidad, ubicacion)
                .map(salas -> new ResponseEntity<>(salas, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
