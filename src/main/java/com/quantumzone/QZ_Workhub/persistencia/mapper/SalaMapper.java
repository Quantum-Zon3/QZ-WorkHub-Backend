package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Sala y SalaDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface SalaMapper {

    /**
     * Convierte Sala a SalaDTO (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - idSala, nombre, capacidad, descripcion, precio
     *
     * CAMPOS IGNORADOS:
     * - reservas: Se ignora para evitar referencia circular
     *   (opcional: exponer solo una lista de ids de reservas en el DTO)
     */
    @Mapping(target = "idSala", source = "idSala")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "capacidad", source = "capacidad")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "precio", source = "precio")
    SalaDto toSalaDto(Sala sala);

    /**
     * Convierte lista de Sala a lista de SalaDTO
     */
    @Mapping(target = "idSala", source = "idSala")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "capacidad", source = "capacidad")
    @Mapping(target = "descripcion", source = "descripcion")
    List<SalaDto> toSalaDtos(List<Sala> salas);

    /**
     * Convierte SalaDTO a Sala (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - idSala: Generado automáticamente con @GeneratedValue
     * - reservas: Se asignan manualmente en la capa de servicio
     */

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "capacidad", source = "capacidad")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "idSala", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Sala toSala(SalaDto salaDto);

    /**
     * Actualiza una Sala existente con datos de SalaDTO
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en SalaDTO es null, no se sobrescribe en la entidad
     */
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "capacidad", source = "capacidad")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "idSala", ignore = true)        // No editable
    @Mapping(target = "reservas", ignore = true)      // Relación manejada aparte
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSala(SalaDto salaDto, @MappingTarget Sala sala);
}

