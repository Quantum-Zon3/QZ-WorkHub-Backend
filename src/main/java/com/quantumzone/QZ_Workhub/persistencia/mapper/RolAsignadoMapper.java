package com.quantumzone.QZ_Workhub.persistencia.mapper;
import com.quantumzone.QZ_Workhub.dominio.dto.RolAsignadoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Rol;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RolAsignado;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
import org.mapstruct.*;
import java.util.List;
/**
 * Mapper para conversiones entre RolAsignado y RolAsignadoDTO usando MapStruct
 *
 * CONFIGURACIÓN:
 * - componentModel = "spring": Crea el mapper como @Component de Spring
 * - unmappedTargetPolicy = WARN: Avisa si hay campos sin mapear
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RolAsignadoMapper {

    /**
     * Convierte rolAsignado a rolAsignadoDto (LECTURA)
     *
     * MAPEO AUTOMÁTICO:
     * - id
     *
     * CAMPOS IGNORADOS:
     */
    @Mapping(target = "idRol", source = "rol",qualifiedByName = "createIdFromRol")
    @Mapping(target = "cedula", source = "usuario",qualifiedByName = "createCedulaFromUsuario")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fechaAsignada", source = "fechaAsignada")
    RolAsignadoDto toRolAsignadoDto(RolAsignado rolAsignado);

    /**
     * Convierte lista de RolAsignado a lista de RolAsignadoDto
     */
    @Mapping(target = "idRol", source = "rol",qualifiedByName = "createIdFromRol")
    @Mapping(target = "cedula", source = "usuario",qualifiedByName = "createCedulaFromUsuario")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fechaAsignada", source = "fechaAsignada")
    List<RolAsignadoDto> toRolAsignadoDtos(List<RolAsignado> rolAsignados);

    /**
     * Convierte RolAsignadoDTO a RolAsignado (CREAR)
     *
     * CAMPOS IGNORADOS:
     * - id: Generado automáticamente con @GeneratedValue
     * - usuario y rol: Se asignan manualmente en la lógica de negocio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", source = "idRol", qualifiedByName = "createRolFromId")
    @Mapping(target = "usuario", source = "cedula", qualifiedByName = "createUsuarioFromCedula")
    @Mapping(target = "fechaAsignada", source = "fechaAsignada")
    RolAsignado toRolAsignado(RolAsignadoDto rolAsignadoDto);

    /**
     * Actualiza un RolAsignado existente con datos de RecursoReservadoDTO
     *
     * ESTRATEGIA NULL_VALUE_PROPERTY_MAPPING_STRATEGY.IGNORE:
     * - Si un campo en RecursoReservadoDTO es null, no se sobrescribe
     */
    @Mapping(target = "id", ignore = true)        // No editable
    @Mapping(target = "rol", ignore = true)   // Relación manejada en el servicio
    @Mapping(target = "usuario", ignore = true)   // Relación manejada en el servicio
    @Mapping(target = "fechaAsignada", source = "fechaAsignada")

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRolAsignado(RolAsignadoDto rolAsignadoDto, @MappingTarget RolAsignado rolAsignado);

    /**
     * METODO AUXILIAR: Crea Usuario con solo la cedula
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createUsuarioFromCedula")
    default Usuario createUsuarioFromCedula(Long cedula) {
        if (cedula == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setCedula(cedula);
        return usuario;
    }

    /**
     * METODO AUXILIAR: obtener la cedula de la usuario
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createCedulaFromUsuario")
    default Long createCedulaFromUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.getCedula();
    }
    /**
     * METODO AUXILIAR: Crea rol con solo la id
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */
    @Named("createRolFromId")
    default Rol createRolFromId(Long idRol) {
        if (idRol == null) {
            return null;
        }
        Rol rol = new Rol();
        rol.setId(idRol);
        return rol;
    }
    /**
     * METODO AUXILIAR: obtener la id del rol
     *
     *
     * @Named: Permite referenciar este metodo en otros mapeos
     */

    @Named("createIdFromRol")
    default Long createIdFromRol(Rol rol) {
        if (rol == null) {
            return null;
        }
        return rol.getId();
    }
}
