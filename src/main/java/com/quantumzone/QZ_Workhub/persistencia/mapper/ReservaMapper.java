package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
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
    @Mapping(target = "idReserva", source = "idReserva")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    @Mapping(target = "cantidadVisitantes", source = "cantidadVisitantes")
    @Mapping(target = "cedula", source = "usuario",qualifiedByName = "createIdFromUsuario")
    @Mapping(target = "idSala", source = "sala",qualifiedByName = "createIdFromSala")
    @Mapping(target = "idPago", source = "pago",qualifiedByName = "createIdFromPago")
    ReservaDto toReservaDto(Reserva reserva);

    /**
     * Convierte lista de Reserva a lista de ReservaDTO
     */
    @Mapping(target = "idReserva", source = "idReserva")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    @Mapping(target = "cantidadVisitantes", source = "cantidadVisitantes")
    @Mapping(target = "cedula", source = "usuario" ,qualifiedByName = "createIdFromUsuario")
    @Mapping(target = "idSala", source = "sala" ,qualifiedByName = "createIdFromSala")
    @Mapping(target = "idPago", source = "pago",qualifiedByName = "createIdFromPago")
    List<ReservaDto> toReservaDtos(List<Reserva> reservas);

    /**
     * Convierte ReservaDTO a Reserva (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idReserva: Generado automáticamente con @GeneratedValue
     * - usuario, sala y listas relacionadas: se asignan manualmente en la capa de servicio
     */

    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    @Mapping(target = "cantidadVisitantes", source = "cantidadVisitantes")
    @Mapping(target = "usuario", source = "cedula",qualifiedByName = "createUsuarioFromId")
    @Mapping(target = "sala", source = "idSala", qualifiedByName = "createSalaFromId")
    @Mapping(target = "pago", source = "idPago", qualifiedByName = "createPagoFromId")
    @Mapping(target = "idReserva", ignore = true)
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
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "montoTotal", source = "montoTotal")
    @Mapping(target = "cantidadVisitantes", source = "cantidadVisitantes")
    @Mapping(target = "idReserva", ignore = true)             // No editable
    @Mapping(target = "usuario", ignore = true)               // Relación manejada aparte
    @Mapping(target = "sala", ignore = true)                  // Relación manejada aparte
    @Mapping(target = "pagos", ignore = true)                 // Listas manejadas aparte
    @Mapping(target = "notificaciones", ignore = true)        // Listas manejadas aparte
    @Mapping(target = "reportes", ignore = true)              // Listas manejadas aparte
    @Mapping(target = "recursosReservados", ignore = true)    // Listas manejadas aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReserva(ReservaDto reservaDto, @MappingTarget Reserva reserva);


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

    @Named("createSalaFromId")
    default Sala createSalaFromId(Long idSala) {
        if (idSala == null) {
            return null;
        }
        Sala sala = new Sala();
        sala.setIdSala(idSala);
        return sala;
    }

    @Named("createIdFromSala")
    default Long createIdFromSala(Sala sala) {
        if (sala == null) {
            return null;
        }
        return sala.getIdSala();
    }

    @Named("createPagoFromId")
    default Pago createPagoFromId(Long idPago) {
        if (idPago == null) {
            return null;
        }
        Pago pago = new Pago();
        pago.setIdPago(idPago);
        return pago;
    }

    @Named("createIdFromPago")
    default Long createIdFromPago(Pago pago) {
        if (pago == null) {
            return null;
        }
        return pago.getIdPago();
    }
}

