package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Recurso y RecursoDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RecursoMapper {

    /**
     * Convierte Recurso a RecursoDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idRecurso, nombre, tipo, unidades, descripcion, precio
     *
     * CAMPOS IGNORADOS:
     * - recursoReservados: No se incluyen en el DTO para evitar referencia circular
     *   (opcionalmente se puede exponer una lista reducida de ids en el DTO)
     */
    @Mapping(target = "recursoReservados", ignore = true)
    RecursoDto toRecursoDto(Recurso recurso);

    /**
     * Convierte lista de Recurso a lista de RecursoDTO
     */
    @Mapping(target = "recursoReservados", ignore = true)
    List<RecursoDto> toRecursoDtos(List<Recurso> recursos);

    /**
     * Convierte RecursoDTO a Recurso (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idRecurso: Generado automáticamente con @GeneratedValue
     * - recursoReservados: Se asignan manualmente en la lógica de negocio
     */
    @Mapping(target = "idRecurso", ignore = true)
    @Mapping(target = "recursoReservados", ignore = true)
    Recurso toRecurso(RecursoDto recursoDto);

    /**
     * Actualiza un Recurso existente con datos de RecursoDTO
     *
     * ¿POR QUÉ @MappingTarget?
     * - Actualiza la entidad existente en lugar de crear una nueva
     * - Permite actualización parcial (solo campos no-null del DTO)
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en RecursoDTO es null, no se sobrescribe en la entidad
     */
    @Mapping(target = "idRecurso", ignore = true)            // No editable
    @Mapping(target = "recursoReservados", ignore = true)    // Relación manejada aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecurso(RecursoDto recursoDto, @MappingTarget Recurso recurso);
}

