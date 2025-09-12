package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.servicio.ReporteService;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
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
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/qzwork_hub/reportes")
@Tag(name = "Reporte", description = "Controlador de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los reportes", description = "Devuelve una lista de todos los reportes registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Reporte>> getAllReportes() {
        return new ResponseEntity<>(reporteService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID", description = "Devuelve un reporte específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    public ResponseEntity<Reporte> getReporteById(
            @PathVariable @Parameter(description = "ID del reporte") Long id) {
        return reporteService.findById(id)
                .map(reporte -> new ResponseEntity<>(reporte, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo reporte con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Reporte> createReporte(
            @RequestBody @Parameter(description = "Datos del reporte a crear") Reporte reporte) {
        Reporte nuevoReporte = reporteService.save(reporte);
        return new ResponseEntity<>(nuevoReporte, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Actualizar un reporte", description = "Actualiza los datos de un reporte existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Reporte> updateReporte(
            @RequestBody @Parameter(description = "Datos actualizados del reporte") Reporte reporte) {
        Reporte updatedReporte = reporteService.update(reporte);
        return new ResponseEntity<>(updatedReporte, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte", description = "Elimina un reporte basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    public ResponseEntity<Void> deleteReporte(
            @PathVariable @Parameter(description = "ID del reporte") Long id) {
        reporteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar reportes por fecha", description = "Busca reportes por una fecha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reportes encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reportes")
    })
    public ResponseEntity<List<Reporte>> buscarReportes(
            @RequestParam @Parameter(description = "Fecha del reporte") LocalDateTime fecha) {

        return reporteService.findByFecha(fecha)
                .map(reportes -> new ResponseEntity<>(reportes, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
