package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Reporte y ReporteDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReporteMapper {

    /**
     * Convierte Reporte a ReporteDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idReporte, motivo, fecha
     *
     * CAMPOS IGNORADOS:
     * - usuario y reserva: Se ignoran para evitar referencia circular
     *   (opcionalmente se pueden exponer como usuarioId y reservaId en el DTO)
     */
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    ReporteDto toReporteDto(Reporte reporte);

    /**
     * Convierte lista de Reporte a lista de ReporteDTO
     */
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    List<ReporteDto> toReporteDtos(List<Reporte> reportes);

    /**
     * Convierte ReporteDTO a Reporte (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idReporte: Generado automáticamente con @GeneratedValue
     * - usuario y reserva: Se asignan manualmente en la capa de servicio
     */
    @Mapping(target = "idReporte", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    Reporte toReporte(ReporteDto reporteDto);

    /**
     * Actualiza un Reporte existente con datos de ReporteDTO
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en ReporteDTO es null, no se sobrescribe en la entidad
     */
    @Mapping(target = "idReporte", ignore = true)  // No se puede editar
    @Mapping(target = "usuario", ignore = true)    // Relación manejada aparte
    @Mapping(target = "reserva", ignore = true)    // Relación manejada aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReporte(ReporteDto reporteDto, @MappingTarget Reporte reporte);
}

