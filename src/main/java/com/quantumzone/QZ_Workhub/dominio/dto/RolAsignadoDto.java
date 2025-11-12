package com.quantumzone.QZ_Workhub.dominio.dto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Rol;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacion del rolAsignado")
public class RolAsignadoDto {

    @Schema(description = "identificador unico", example = "1", required = true)
    private Long id;

    @Schema(description = "Fecha de asignaciòn del rol", example = "2025-09-18T11:30:00", required = true)
    private LocalDateTime fechaAsignada;

    @Schema(description = "id del Rol que se asignò", example = "2", required = true)
    private Long idRol;

    @Schema(description = "Cedula del Usuario al que se le asignarà el rol", example = "1", required = true)
    private Long cedula;

}
