package com.quantumzone.QZ_Workhub.web.controlador;
import java.time.LocalDate;
import java.util.List;

import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
import com.quantumzone.QZ_Workhub.dominio.servicio.UsuarioService;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;

//imports de anotacion springboot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService ) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDto>> findAll() {
        return new ResponseEntity<>(usuarioService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioDto> findById(@PathVariable @Parameter(description = "ID del usuario") Long id) {
        try {
            UsuarioDto product = usuarioService.findById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<UsuarioDto> save(
            @RequestBody @Parameter(description = "Datos del usuario a crear") UsuarioDto usuario) {
        UsuarioDto nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<UsuarioDto> update(
            @RequestBody @Parameter(description = "Datos actualizados del usuario") UsuarioDto usuario) {
        UsuarioDto updatedUsuario = usuarioService.update(usuario.getCedula(), usuario);
        return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Parameter(description = "ID del usuario") Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /*
    @GetMapping("/rol")
    @Operation(summary = "Buscar usuarios por rol", description = "Devuelve los usuarios asociados a un rol específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con ese rol")
    })
    public ResponseEntity<List<UsuarioDto>> findByRol(
            @RequestParam @Parameter(description = "Rol del usuario") Rol rol) {

        return usuarioService.findUsuarioByRol(rol)
                .map(usuarios -> new ResponseEntity<>(usuarios, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
     */
}
