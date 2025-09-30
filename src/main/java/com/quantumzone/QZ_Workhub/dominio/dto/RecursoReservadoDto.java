package com.quantumzone.QZ_Workhub.dominio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informacion de recursos reservados")
public class RecursoReservadoDto {
    @Schema(description = "identificador de un recursoReservado", example = "1")
    private Long id;

    @Schema(description = "Cantidad del tipo de recurso a utilizar", example = "3")
    private Long cantidad;

    @Schema(description = "Fecha en la que se utiliza el recurso", example = "26-09-2025")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha en la quer se dejo de utilizar el recurso", example = "26-09-2025")
    private LocalDateTime fechaFin;

    @Schema(description = "Monto total por la cantidad de recursos a utilizar de este tipo", example = "200000")
    private Integer montoTotal;
    
    @Schema(description = "ID del recurso solicitado", example = "1", required = true)
    private Long idRecursoReservado;

    @Schema(description = "ID de la reserva creada", example = "1", required = true)
    private Long idReserva;
}
