package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Reserva y ReservaDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReservaMapper {

    /**
     * Convierte Reserva a ReservaDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idReserva, fechaInicio, fechaFin, montoTotal, cantidadVisitantes
     *
     * CAMPOS IGNORADOS:
     * - usuario, sala, pagos, notificaciones, reportes, recursosReservados
     *   (para evitar ciclos. Opcional: exponer solo los IDs en el DTO)
     */
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "pagos", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "recursosReservados", ignore = true)
    ReservaDto toReservaDto(Reserva reserva);

    /**
     * Convierte lista de Reserva a lista de ReservaDTO
     */
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "pagos", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "recursosReservados", ignore = true)
    List<ReservaDto> toReservaDtos(List<Reserva> reservas);

    /**
     * Convierte ReservaDTO a Reserva (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idReserva: Generado automáticamente con @GeneratedValue
     * - usuario, sala y listas relacionadas: se asignan manualmente en la capa de servicio
     */
    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "pagos", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "recursosReservados", ignore = true)
    Reserva toReserva(ReservaDto reservaDto);

    /**
     * Actualiza una Reserva existente con datos de ReservaDTO
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en ReservaDTO es null, no se sobrescribe en la entidad
     */
    @Mapping(target = "idReserva", ignore = true)             // No editable
    @Mapping(target = "usuario", ignore = true)               // Relación manejada aparte
    @Mapping(target = "sala", ignore = true)                  // Relación manejada aparte
    @Mapping(target = "pagos", ignore = true)                 // Listas manejadas aparte
    @Mapping(target = "notificaciones", ignore = true)        // Listas manejadas aparte
    @Mapping(target = "reportes", ignore = true)              // Listas manejadas aparte
    @Mapping(target = "recursosReservados", ignore = true)    // Listas manejadas aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReserva(ReservaDto reservaDto, @MappingTarget Reserva reserva);
}

