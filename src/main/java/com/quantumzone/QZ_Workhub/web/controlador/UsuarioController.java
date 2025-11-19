package com.quantumzone.QZ_Workhub.web.controlador;
import java.util.List;

import com.quantumzone.QZ_Workhub.dominio.dto.LoginRequest;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.UsuarioService;
//imports de anotacion springboot
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//imports para documentar swagen
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/qzwork_hub/usuarios")
@Tag(name = "Usuario", description = "Controlador de usuarios")
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService ) {
        this.usuarioService = usuarioService;
    }

    /**
     * Crear un nuevo Usuario
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crea un nuevo usaurio en el sistema con validación de email único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o email duplicado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UsuarioDto> save(
            @Parameter(description = "Datos del usaurio a crear", required = true)
            @RequestBody UsuarioDto createDTO
    ) {
        log.info("POST /qzwork_hub/usuarios - Creando Usuario: {}", createDTO.getEmail());

        try {
            UsuarioDto usuarioCreado = usuarioService.save(createDTO);
            log.info("usuario creado exitosamente con ID: {}", usuarioCreado.getCedula());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear usario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener todos los Usuario
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Obtiene la lista completa de usuario registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuarios no encontrado"
            )
    })
    public ResponseEntity<List<UsuarioDto>> finAll() {
        log.debug("GET /qzwork_hub/usuarios - Obteniendo todos los usaurio");
        try {
            List<UsuarioDto> usaurios = usuarioService.findAll();
            log.debug("Se encontraron {} usaurios", usaurios.size());
            return ResponseEntity.ok(usaurios);
        }catch (EmptyResultDataAccessException e){
            log.error("No se encontro el usaurio exitosamente");
            return ResponseEntity.notFound().build();
        }

    }


    /**
     * Obtener Usuairo por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Usuario por ID",
            description = "Obtiene la información completa de un usuario específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<UsuarioDto> findById(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /qzwork_hub/usuarios/{} - Buscando usuario", id);

        try {
            UsuarioDto usaurio = usuarioService.findById(id);
            return ResponseEntity.ok(usaurio);
        } catch (RuntimeException e) {
            log.warn("Usuario no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Actualizar usuario existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usaurio",
            description = "Actualiza la información de un usuario existente. El email no se puede modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<UsuarioDto> update(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar del usuario", required = true)
            @RequestBody UsuarioDto updateDTO
    ) {
        log.info("PUT /qzwork_hub/usuarios/{} - Actualizando usuario", id);

        try {
            UsuarioDto usuarioActualizado = usuarioService.update(id, updateDTO);
            log.info("Usuario actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Usuario no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar usaurio ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema. No se puede eliminar si tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: usuario tiene reservas asociados"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /qzwork_hub/usuarios/{} - Eliminando usuario", id);

        try {
            usuarioService.deleteUsuario(id);
            log.info("Usuario eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Usuairo no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("reserva")) {
                log.warn("Intento de usaurio con reservas ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar usuario ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody LoginRequest request) {

        UsuarioDto usuario = usuarioService.loguear(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(usuario);
    }
}
