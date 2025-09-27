package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
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
    @Mapping(target = "cedula", source = "usuario", qualifiedByName = "createIdFromUsuario")
    @Mapping(target = "idReserva", source = "reserva", qualifiedByName = "createIdFromReserva")
    @Mapping(target = "idReporte", source = "idReporte")
    @Mapping(target = "motivo", source = "motivo")
    @Mapping(target = "fecha", source = "fecha")
    ReporteDto toReporteDto(Reporte reporte);

    /**
     * Convierte lista de Reporte a lista de ReporteDTO
     */
    @Mapping(target = "cedula", source = "usuario", qualifiedByName = "createIdFromUsuario")
    @Mapping(target = "idReserva", source = "reserva", qualifiedByName = "createIdFromReserva")
    @Mapping(target = "idReporte", source = "idReporte")
    @Mapping(target = "motivo", source = "motivo")
    @Mapping(target = "fecha", source = "fecha")
    List<ReporteDto> toReporteDtos(List<Reporte> reportes);

    /**
     * Convierte ReporteDTO a Reporte (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idReporte: Generado automáticamente con @GeneratedValue
     * - usuario y reserva: Se asignan manualmente en la capa de servicio
     */
    @Mapping(target = "usuario", source = "cedula", qualifiedByName = "createUsuarioFromId")
    @Mapping(target = "reserva", source = "idReserva", qualifiedByName = "createReservaFromId")
    @Mapping(target = "idReporte", ignore = true)
    @Mapping(target = "motivo", source = "motivo")
    @Mapping(target = "fecha", source = "fecha")
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
    @Mapping(target = "motivo", source = "motivo")
    @Mapping(target = "fecha", source = "fecha")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReporte(ReporteDto reporteDto, @MappingTarget Reporte reporte);

    @Named("createReservaFromId")
    default Reserva createReservaFromId(Long idReserva) {
        if (idReserva == null) {
            return null;
        }
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        return reserva;
    }

    @Named("createIdFromReserva")
    default Long createIdFromReserva(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return reserva.getIdReserva();
    }

    @Named("createUsuarioFromId")
    default Usuario createUsuarioFromId(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setCedula(idUsuario);
        return usuario;
    }

    @Named("createIdFromUsuario")
    default Long createIdFromUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.getCedula();
    }
}

