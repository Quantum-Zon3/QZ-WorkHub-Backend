package com.quantumzone.QZ_Workhub.web.controlador;
//imports de anotacion springboot
import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReporteService;
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
@RequestMapping("/qzwork_hub/reportes")
@Tag(name = "Reporte", description = "Controlador de reportes")
@Slf4j
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Obtener todas las Reporte
     */
    @GetMapping
    @Operation(
            summary = "Listar todas las reporte",
            description = "Obtiene la lista completa de productos con información del reporte"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reporte obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reporte no encontrado"
            )
    })
    public ResponseEntity<List<ReporteDto>> findAll() {
        log.debug("GET /qzwork_hub/reportes - Obteniendo todos los reportes");
        try{
            List<ReporteDto> reportes = reporteService.findAll();
            log.debug("Se encontraron {} reportes", reportes.size());
            return ResponseEntity.ok(reportes);
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Obtener reporte por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reporte por ID",
            description = "Obtiene la información completa de un reporte"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reporte no encontrado"
            )
    })
    public ResponseEntity<ReporteDto> findById(
            @Parameter(description = "ID del reporte", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/reporte{{} - Buscando reporte", id);

        try {
            ReporteDto reporteDto = reporteService.findById(id);
            return ResponseEntity.ok(reporteDto);
        } catch (RuntimeException e) {
            log.warn("reporte no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear una nueva reporte
     */
    @PostMapping
    @Operation(
            summary = "Crear nueva reporte",
            description = "Crea un nuevo reporte"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "reporte creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteDto.class)
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
    public ResponseEntity<ReporteDto> save(
            @Parameter(description = "Datos de la reporte a crear", required = true)
            @RequestBody ReporteDto createDTO
    ) {
        log.info("POST /qzwork_hub/reportes - Creando reporte: {}", createDTO.getMotivo());

        try {
            ReporteDto reporteCreado = reporteService.save(createDTO);
            log.info("Reporte creado exitosamente con ID: {}", reporteCreado.getIdReporte());
            return ResponseEntity.status(HttpStatus.CREATED).body(reporteCreado);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear reporte: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * Eliminar reporte
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar reporte",
            description = "Elimina una reporte del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Reporte eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reporte no encontrada"
            )
    })
    public ResponseEntity<Void> deleteReporte(
            @Parameter(description = "ID del reporte a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/reportes/{} - Eliminando reporte", id);

        try {
            reporteService.deleteReporte(id);
            log.info("Reporte eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("reporte no encontrado para eliminar ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualizar reporte existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar reporte",
            description = "Actualiza la información de un reporte."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reporte no encontrado"
            )
    })
    public ResponseEntity<ReporteDto> updateReporte(
            @Parameter(description = "ID del Reporte a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del reporte", required = true)
            @RequestBody ReporteDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/reportes/{} - Actualizando reporte", id);

        try {
            ReporteDto updatedProduct = reporteService.update(id, updateDTO);
            log.info("reporte actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("reporte no encontrada para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar reporter ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /*
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
    }*/
}
