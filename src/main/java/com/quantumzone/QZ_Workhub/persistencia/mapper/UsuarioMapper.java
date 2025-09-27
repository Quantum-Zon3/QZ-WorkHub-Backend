package com.quantumzone.QZ_Workhub.persistencia.mapper;

import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre Usuario y usuarioDTO usando MapStruct
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {
    /**
     * Convierte Usuario a usuarioDTO  (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - Todos los campos con mismo nombre se mapean automáticamente
     * - cedula, nombre, apellido, email, rol, contraseña, fecha registro, telefono
     *
     * CAMPOS IGNORADOS:
     * - reservas: No los incluimos en el DTO para evitar referencia circular
     * - reportes: No los incluimos en el DTO para evitar referencia circular
     */
    @Mapping(target  = "cedula", source = "cedula")
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "contraseña", source = "contraseña")
    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    @Mapping(target = "telefono", source = "telefono")
    UsuarioDto toUsuarioDto(Usuario usuario);

    /**
     * Convierte lista de Usuarios a lista de usuarioDTO
     */
    @Mapping(target  = "cedula", source = "cedula")
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "contraseña", source = "contraseña")
    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    @Mapping(target = "telefono", source = "telefono")
    List<UsuarioDto> toUsuarioDtos(List<Usuario> usuarios);

    /**
     * Convierte UsaurioDto a Usuario (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - createdAt/updatedAt: Los maneja automáticamente JPA
     * - reserva: Lista vacía por defecto
     *
     * MAPEO AUTOMÁTICO:
     * - name, email, phone, address se mapean automáticamente
     */

    @Mapping(target  = "cedula", source = "cedula")
    @Mapping(target = "nombre", source = "nombre" )
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "contraseña", source = "contraseña")
    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    @Mapping(target = "telefono", source = "telefono")
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    Usuario toUsuario(UsuarioDto usuarioDto);

    /**
     * Actualiza Usuario existente con datos de UsuarioDTO
     *
     * ¿POR QUÉ @MappingTarget?
     * - Actualiza la entidad existente en lugar de crear una nueva
     * - Permite actualización parcial (solo campos no-null del DTO)
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en UsuarioDTO es null, no actualiza ese campo en la entity
     * - Permite actualización parcial (PATCH)
     */
    @Mapping(target = "cedula", ignore = true) // No puede edtiar su cedula
    @Mapping(target = "email", ignore = true)  // No puede edtiar su email
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "contraseña", source = "contraseña")
    @Mapping(target = "telefono", source = "telefono")
    @Mapping(target = "", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUsuario(UsuarioDto usuarioDto, @MappingTarget Usuario usuario);

}
