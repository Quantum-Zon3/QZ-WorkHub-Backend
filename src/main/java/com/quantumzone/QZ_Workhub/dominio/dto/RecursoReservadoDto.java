package com.quantumzone.QZ_Workhub.dominio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informacion de recursos reservados")
public class RecursoReservadoDto {
    @Schema(description = "identificador de un recursoReservado", example = "1")
    private Long id;

    @Schema(description = "ID del recurso solicitado", example = "1", required = true)
    private Long idRecursoReservado;

    @Schema(description = "ID de la reserva creada", example = "1", required = true)
    private Long idReserva;
}
