package com.quantumzone.QZ_Workhub.dominio.dto;

import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "información de recurso")
public class RecursoDto {
    @Schema(description = "identificador de recurso", example = "1")
    private Long idRecurso;

    @Schema(description = "nombre del recurso", example = "audifonos SonyQuality", required = true)
    private String nombre;

    @Schema(description = "tipo de recurso", example = "electronico", required = true)
    private TipoRecurso tipoRecurso;

    @Schema(description = "cantidad de unidades del recurso", example = "10", required = true)
    private Integer unidades;

    @Schema(description = "descripcion de el recurso", example = "rojo con cable usb multitarea", required = true, maxLength = 200)
    private String descripcion;

    @Schema(description = "precio de el recurso", example = "5700", required = true)
    private Float precio;
}
