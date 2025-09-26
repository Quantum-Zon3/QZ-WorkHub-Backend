package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Pago y PagoDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PagoMapper {

    /**
     * Convierte Pago a PagoDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idPago, monto, fechaPago, metodoPago, estadoPago
     *
     * CAMPOS ESPECIALES:
     * - reserva: Se ignora para evitar referencia circular
     *   (opcionalmente se puede mapear solo el idReserva si lo agregas en el DTO)
     */
    @Mapping(target = "reserva", ignore = true)
    PagoDto toPagoDto(Pago pago);

    /**
     * Convierte lista de Pago a lista de PagoDTO
     */
    @Mapping(target = "reserva", ignore = true)
    List<PagoDto> toPagoDtos(List<Pago> pagos);

    /**
     * Convierte PagoDTO a Pago (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idPago: Generado automáticamente con @GeneratedValue
     *
     * MANEJO DE RELACIONES:
     * - reserva: Se asigna manualmente en el servicio
     */
    @Mapping(target = "idPago", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    Pago toPago(PagoDto pagoDto);

    /**
     * Actualiza un Pago existente con datos de PagoDTO
     *
     * ¿POR QUÉ @MappingTarget?
     * - Actualiza la entidad existente en lugar de crear una nueva
     * - Permite actualización parcial (PATCH)
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en PagoDTO es null, no se sobrescribe en la entidad
     */
    @Mapping(target = "idPago", ignore = true)     // No editable
    @Mapping(target = "reserva", ignore = true)    // Relación manejada aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePago(PagoDto pagoDto, @MappingTarget Pago pago);
}

