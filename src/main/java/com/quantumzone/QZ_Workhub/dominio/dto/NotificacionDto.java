package com.quantumzone.QZ_Workhub.dominio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "descripcion de notificacion")
public class NotificacionDto {
    @Schema(description = "id de notificacion", example = "1")
    private Long idNotificacion;

    @Schema(description = "motivo de la notificaion", example = "Reserva confirmada", required = true, maxLength = 45)
    private String motivo;

    @Schema(description = "fecha de envio de la notificacion", example = "2025-09-18T11:30:00", required = true)
    private LocalDateTime fecha;

    @Schema(description = "mensaje de la noti", example = "Su reserva a sido confirmada a las 14:00 el martes", required = true, maxLength = 200)
    private String descripcion;

    @Schema(description = "ID de la reserva que se esta haciendo", example = "1", required = true)
    private Long idReserva;
}
