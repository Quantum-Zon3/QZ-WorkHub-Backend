package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
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
    @Mapping(target = "idPago", source = "idPago")
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fechaRealizacion", source = "fechaPago")
    @Mapping(target = "metodoPago", source = "metodoPago")
    @Mapping(target = "estadoPago", source = "estadoPago")
    @Mapping(target = "idReserva", source = "reserva", qualifiedByName = "createIdFromReserva")
    PagoDto toPagoDto(Pago pago);

    /**
     * Convierte lista de Pago a lista de PagoDTO
     */
    @Mapping(target = "idPago", source = "idPago")
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fechaRealizacion", source = "fechaPago")
    @Mapping(target = "metodoPago", source = "metodoPago")
    @Mapping(target = "estadoPago", source = "estadoPago")
    @Mapping(target = "idReserva", source = "reserva", qualifiedByName = "createIdFromReserva")
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
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fechaPago", source = "fechaRealizacion")
    @Mapping(target = "metodoPago", source = "metodoPago")
    @Mapping(target = "estadoPago", source = "estadoPago")
    @Mapping(target = "reserva", source = "idReserva", qualifiedByName = "createReservaFromId")
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
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fechaPago", source = "fechaRealizacion")
    @Mapping(target = "metodoPago", source = "metodoPago")
    @Mapping(target = "estadoPago", source = "estadoPago")
    @Mapping(target = "idPago", ignore = true)     // No editable
    @Mapping(target = "reserva", ignore = true)    // Relación manejada aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePago(PagoDto pagoDto, @MappingTarget Pago pago);

    /**
     * METODO AUXILIAR: Crea reserva con solo el ID
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createReservaFromId")
    default Reserva createReservaFromId(Long idReserva) {
        if (idReserva == null) {
            return null;
        }
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        return reserva;
    }
    /**
     * METODO AUXILIAR: obtener la id de la reserva
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createIdFromReserva")
    default Long createIdFromReserva(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return reserva.getIdReserva();
    }
}

