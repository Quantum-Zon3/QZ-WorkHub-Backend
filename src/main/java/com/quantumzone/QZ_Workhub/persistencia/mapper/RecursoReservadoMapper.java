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
    @Mapping(target = "idReserva", source = "reserva")
    @Mapping(target = "idRecursoReservado", source = "recurso")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "recurso")
    @Mapping(target = "fechaFin", source = "recurso")
    @Mapping(target = "montoTotal", source = "recurso")
    RecursoReservadoDto toRecursoReservadoDto(RecursoReservado recursoReservado);

    /**
     * Convierte lista de RecursoReservado a lista de RecursoReservadoDTO
     */
    @Mapping(target = "idReserva", source = "reserva")
    @Mapping(target = "idRecursoReservado", source = "recurso")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "recurso")
    @Mapping(target = "fechaFin", source = "recurso")
    @Mapping(target = "montoTotal", source = "recurso")
    List<RecursoReservadoDto> toRecursoReservadoDtos(List<RecursoReservado> recursoReservados);

    /**
     * Convierte RecursoReservadoDTO a RecursoReservado (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - id: Generado automáticamente con @GeneratedValue
     * - recurso y reserva: Se asignan manualmente en la lógica de negocio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurso", source = "idRecursoReservado")
    @Mapping(target = "reserva", source = "idReserva")
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
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
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecursoReservado(RecursoReservadoDto recursoReservadoDto, @MappingTarget RecursoReservado recursoReservado);
}

