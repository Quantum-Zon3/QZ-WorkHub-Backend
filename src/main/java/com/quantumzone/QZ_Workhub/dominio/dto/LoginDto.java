package com.quantumzone.QZ_Workhub.dominio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login del usuario")
public class LoginDto {
    @Schema(description = "email del usuario", example = "eam@gmail.com")
    private String email;
    @Schema(description = "contraseña del user", example = "michigan")
    private String password;
}
