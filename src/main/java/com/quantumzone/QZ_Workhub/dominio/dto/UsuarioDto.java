package com.quantumzone.QZ_Workhub.dominio.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacion del usuario")
public class UsuarioDto {
    @Schema(description = "Id única del usuario", example = "1")
    private Long cedula;

    @Schema(description = "Nombre del usuario", example = "Juanito", required = true, maxLength = 45)
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Ruiz", required = true, maxLength = 45)
    private String apellido;

    @Schema(description = "Email del usuario", example = "juan@gmail.com", required = true, maxLength = 45)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "mimamamemima", required = true, maxLength = 45)
    private String contraseña;

    @Schema(description = "Fecha de registro del usuario", example = "2025-09-18T10:30:00", required = true)
    private LocalDateTime fechaRegistro;

    @Schema(description = "Telefono del usuario", example = "309 8634519", required = true, maxLength = 45)
    private String telefono;

}
