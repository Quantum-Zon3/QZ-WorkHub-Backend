package com.quantumzone.QZ_Workhub.dominio.dto;

import com.quantumzone.QZ_Workhub.dominio.enums.EstadoPago;
import com.quantumzone.QZ_Workhub.dominio.enums.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "informacion de pago")
public class PagoDto {
    @Schema(description = "id del pago")
    private Long idPago;

    @Schema(description = "monto a pagar", example = "79000", required = true)
    private Double monto;

    @Schema(description = "fecha de realización del pago", example = "2025-09-18T11:30:00", required = true)
    private LocalDateTime fechaRealizacion;

    @Schema(description = "metodo de pago", example = "Tarjeta", required = true, maxLength = 45)
    private MetodoPago metodoPago;

    @Schema(description = "estado de pago", example = "Realizado", required = true)
    private EstadoPago estadoPago;
}
