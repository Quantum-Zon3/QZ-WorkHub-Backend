package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre RecursoReservado y RecursoReservadoDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RecursoReservadoMapper {

    /**
     * Convierte RecursoReservado a RecursoReservadoDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - id
     *
     * CAMPOS IGNORADOS:
     * - recurso y reserva: Se ignoran para evitar referencia circular
     *   (opcional: puedes exponer solo idRecurso y idReserva en el DTO)
     */
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    RecursoReservadoDto toRecursoReservadoDto(RecursoReservado recursoReservado);

    /**
     * Convierte lista de RecursoReservado a lista de RecursoReservadoDTO
     */
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    List<RecursoReservadoDto> toRecursoReservadoDtos(List<RecursoReservado> recursoReservados);

    /**
     * Convierte RecursoReservadoDTO a RecursoReservado (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - id: Generado automáticamente con @GeneratedValue
     * - recurso y reserva: Se asignan manualmente en la lógica de negocio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    RecursoReservado toRecursoReservado(RecursoReservadoDto recursoReservadoDto);

    /**
     * Actualiza un RecursoReservado existente con datos de RecursoReservadoDTO
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en RecursoReservadoDTO es null, no se sobrescribe
     */
    @Mapping(target = "id", ignore = true)        // No editable
    @Mapping(target = "recurso", ignore = true)   // Relación manejada en el servicio
    @Mapping(target = "reserva", ignore = true)   // Relación manejada en el servicio
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecursoReservado(RecursoReservadoDto recursoReservadoDto, @MappingTarget RecursoReservado recursoReservado);
}

