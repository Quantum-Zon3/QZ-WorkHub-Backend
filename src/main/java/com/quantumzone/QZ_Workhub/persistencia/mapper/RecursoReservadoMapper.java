package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
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
    @Mapping(target = "idReserva", source = "reserva",qualifiedByName = "createIdFromReserva")
    @Mapping(target = "idRecurso", source = "recurso",qualifiedByName = "createIdFromRecurso")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    RecursoReservadoDto toRecursoReservadoDto(RecursoReservado recursoReservado);

    /**
     * Convierte lista de RecursoReservado a lista de RecursoReservadoDTO
     */
    @Mapping(target = "idReserva", source = "reserva", qualifiedByName = "createIdFromReserva")
    @Mapping(target = "idRecurso", source = "recurso",qualifiedByName = "createIdFromRecurso")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    List<RecursoReservadoDto> toRecursoReservadoDtos(List<RecursoReservado> recursoReservados);

    /**
     * Convierte RecursoReservadoDTO a RecursoReservado (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - id: Generado automáticamente con @GeneratedValue
     * - recurso y reserva: Se asignan manualmente en la lógica de negocio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurso", source = "idRecurso", qualifiedByName = "createRecursoFromId")
    @Mapping(target = "reserva", source = "idReserva", qualifiedByName = "createReservaFromId")
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
    /**
     * METODO AUXILIAR: Crea reserva con solo el ID
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createRecursoFromId")
    default Recurso createRecursoFromId(Long idRecurso) {
        if (idRecurso == null) {
            return null;
        }
        Recurso recurso = new Recurso();
        recurso.setIdRecurso(idRecurso);
        return recurso;
    }
    /**
     * METODO AUXILIAR: obtener la id de la reserva
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */

    @Named("createIdFromRecurso")
    default Long createIdFromRecurso(Recurso recurso) {
        if (recurso == null) {
            return null;
        }
        return recurso.getIdRecurso();
    }

}

