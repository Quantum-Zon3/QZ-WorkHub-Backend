package com.quantumzone.QZ_Workhub.dominio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacion de la sala")
public class SalaDto {
    @Schema(description = "Identificador único de la sala", example = "1")
    private Long idSala;

    @Schema(description = "nombre que recibe la sala", example = "sala de audiovisuales", required = true, maxLength = 45)
    private String nombre;

    @Schema(description = "capacidad de ocupantes en la sala", example = "10", required = true)
    private Integer capacidad;

    @Schema(description = "descripcion de la sala", example = "amplia con ventanales grandes", required = true, maxLength = 45)
    private String descripcion;

    @Schema(description = "precio de renta del espacio", example = "20000", required = true)
    private Float precio;
}
