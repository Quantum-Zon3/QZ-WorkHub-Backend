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
    @Schema(description = "identificador de un recursoReservado")
    private Long id;
}
