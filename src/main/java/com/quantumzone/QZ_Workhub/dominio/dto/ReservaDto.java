package com.quantumzone.QZ_Workhub.dominio.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de la reserva")
public class ReservaDto {
    @Schema(description = "id único para la reserva", example = "1")
    private Long idReserva;

    @Schema(description = "fecha de inicio de la reserva", example = "2025-09-18T10:30:00", required = true)
    private LocalDateTime fechaInicio;

    @Schema(description = "fecha de fin de la reserva", example = "2025-09-18T11:30:00", required = true)
    private LocalDateTime fechaFin;

    @Schema(description = "monto total de la reserva", example = "43000", required = true)
    private Double montoTotal;

    @Schema(description = "cantidad de visitantes en la reserva", example = "10", required = true)
    private Integer cantidadVisitantes;
}
