package com.quantumzone.QZ_Workhub.dominio.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacion del rol")
public class RolDto {

    @Schema(description = "Id única del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "Administrador", required = true, maxLength = 45)
    private String nombre;

    @Schema(description = "Descripcion del id", example = "Ruiz", required = true, maxLength = 100)
    private String descripcion;

}
