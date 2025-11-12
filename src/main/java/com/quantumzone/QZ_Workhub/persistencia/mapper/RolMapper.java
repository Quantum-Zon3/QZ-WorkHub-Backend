package com.quantumzone.QZ_Workhub.persistencia.mapper;

import com.quantumzone.QZ_Workhub.dominio.dto.RolDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Rol;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Rol y RolDTO usando MapStruct
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RolMapper {
    /**
     * Convierte Rol a rolDTO  (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - Todos los campos con mismo nombre se mapean automáticamente
     * - id, nombre, desdripcion
     *
     * CAMPOS IGNORADOS:
     * - rolesAsignados: No los incluimos en el DTO para evitar referencia circular
     */
    @Mapping(target  = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "descripcion", source = "descripcion")
    RolDto toRolDto(Rol rol);

    /**
     * Convierte lista de Roles a lista de RolesDto
     */
    @Mapping(target  = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "descripcion", source = "descripcion")
    List<RolDto> toRolDtos(List<Rol> roles);

    /**
     * Convierte RolDto a rol (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - createdAt/updatedAt: Los maneja automáticamente JPA
     * - rolesAsignados: Lista vacía por defecto
     * - id autogenerada
     */
    @Mapping(target  = "id", ignore = true )
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "rolesAsignados", ignore = true)
    Rol toRol(RolDto rolDto);

    /**
     * Actualiza Rol existente con datos de rolDto
     *
     * ¿POR QUÉ @MappingTarget?
     * - Actualiza la entidad existente en lugar de crear una nueva
     * - Permite actualización parcial (solo campos no-null del DTO)
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en RolDTO es null, no actualiza ese campo en la entity
     * - Permite actualización parcial (PATCH)
     */
    @Mapping(target = "id", ignore = true) // No puede edtiar su id
    @Mapping(target = "rolesAsignados", ignore = true)
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "descripcion", source = "descripcion")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRol(RolDto rolDto, @MappingTarget Rol rol);

}
