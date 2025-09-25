package com.quantumzone.QZ_Workhub.dominio.dto;

import com.quantumzone.QZ_Workhub.dominio.enums.MotivoReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informacion de reporte")
public class ReporteDto {
    @Schema(description = "identificador único de un reporte", example = "1")
    private Long idReporte;

    @Schema(description = "motivo del reporte", required = true)
    private MotivoReporte motivo;

    @Schema(description = "fecha del reporte", example = "2025-09-18T11:30:00", required = true)
    private LocalDateTime fecha;

    @Schema(description = "Cedula del usuario del reporte", example = "1094897822", required = true)
    private Long cedula;

    @Schema(description = "ID de la reserva creada", example = "1", required = true)
    private Long idReserva;
}
