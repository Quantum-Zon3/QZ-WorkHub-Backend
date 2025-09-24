package com.quantumzone.QZ_Workhub.persistencia.mapper;

import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Notificacion y NotificacionDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificacionMapper {

    /**
     * Convierte Notificacion a NotificacionDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idNotificacion, motivo, fecha, descripcion
     *
     * CAMPOS ESPECIALES:
     * - reserva: Se ignora para evitar referencia circular.
     *   (Si necesitas mostrar info básica de la reserva, puedes crear un ReservaDto reducido)
     */
    NotificacionDto toNotificacionDto(Notificacion notificacion);

    /**
     * Convierte lista de Notificacion a lista de NotificacionDTO
     */
    List<NotificacionDto> toNotificacionDtos(List<Notificacion> notificaciones);

    /**
     * Convierte NotificacionDTO a Notificacion (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idNotificacion: Se genera automáticamente con @GeneratedValue
     *
     * MANEJO DE RELACIONES:
     * - reserva: Se debe asignar manualmente en el servicio si es necesario
     */
    @Mapping(target = "idNotificacion", ignore = true)
    @Mapping(target = "reserva", source = "id_reserva", qualifiedByName = "crearReservaPorId")
    Notificacion toNotificacion(NotificacionDto notificacionDto);

    /**
     * Actualiza Notificacion existente con datos de NotificacionDTO
     *
     * ¿POR QUÉ @MappingTarget?
     * - Actualiza la entidad existente en lugar de crear una nueva
     * - Permite actualización parcial (solo campos no-null del DTO)
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en NotificacionDTO es null, no se sobrescribe
     */
    @Mapping(target = "idNotificacion", ignore = true) // No puede editar el id
    @Mapping(target = "reserva", ignore = true)        // Relación manejada en servicio
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNotificacion(NotificacionDto notificacionDto, @MappingTarget Notificacion notificacion);
    /**
     * METODO AUXILIAR: Crea SellerEntity con solo el ID
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("crearReservaPorId")
    default Reserva createReservaFromId(Long idReserva) {
        if (idReserva == null) {
            return null;
        }
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        return reserva;
    }
}

