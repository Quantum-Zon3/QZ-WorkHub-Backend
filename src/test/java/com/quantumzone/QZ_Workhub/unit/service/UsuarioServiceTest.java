package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReporteService;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.dominio.servicio.UsuarioService;
import com.quantumzone.QZ_Workhub.persistencia.dao.UsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - unit Tests")
public class UsuarioServiceTest {

    @Mock
    UsuarioDAO usuarioDAO;

    @Mock
    Clock clock;

    @Mock
    ReporteService reporteService;

    @Mock
    ReservaService reservaService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Long idValido;
    private UsuarioDto usuarioDtoValido;

    @BeforeEach
    void setUp() {
        idValido = 1L;
        usuarioDtoValido = new UsuarioDto();

        Instant fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        usuarioDtoValido.setCedula(idValido);
        usuarioDtoValido.setNombre("Nombre");
        usuarioDtoValido.setApellido("Apellido");
        usuarioDtoValido.setEmail("example@example.com");
        usuarioDtoValido.setRol(Rol.MIEMBRO);
        usuarioDtoValido.setContraseña("12345");
        usuarioDtoValido.setFechaRegistro(LocalDateTime.now(clock));
        usuarioDtoValido.setTelefono("123456789");
    }

    @Test
    @DisplayName("Debe lanzar excepción si la cédula es nula o <= 0")
    void validarUsuario_CedulaInvalida() {
        usuarioDtoValido.setCedula(0L);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("La cédula es obligatoria y debe ser un número positivo", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre es vacío")
    void validarUsuario_NombreVacio() {
        usuarioDtoValido.setNombre(" ");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El nombre del usuario es obligatorio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre excede 45 caracteres")
    void validarUsuario_NombreLargo() {
        usuarioDtoValido.setNombre("A".repeat(46));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El nombre no puede exceder 45 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el apellido es vacío o largo")
    void validarUsuario_ApellidoInvalido() {
        usuarioDtoValido.setApellido(" ");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El apellido del usuario es obligatorio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email es inválido")
    void validarUsuario_EmailInvalido() {
        usuarioDtoValido.setEmail("correo@");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El formato del email no es válido", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol es nulo")
    void validarUsuario_RolNulo() {
        usuarioDtoValido.setRol(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El rol del usuario es obligatorio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la contraseña no cumple requisitos")
    void validarUsuario_ContrasenaInvalida() {
        usuarioDtoValido.setContraseña("abc"); // corta y sin números

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("La contraseña debe tener entre 8 y 45 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el teléfono tiene caracteres inválidos")
    void validarUsuario_TelefonoInvalido() {
        usuarioDtoValido.setCedula(1L);
        usuarioDtoValido.setNombre("Juan");
        usuarioDtoValido.setApellido("Pérez");
        usuarioDtoValido.setEmail("juan@example.com");
        usuarioDtoValido.setRol(Rol.MIEMBRO);
        usuarioDtoValido.setContraseña("abc12345");
        usuarioDtoValido.setTelefono("abc123");
        usuarioDtoValido.setFechaRegistro(LocalDateTime.now(clock));


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El teléfono solo puede contener números, espacios, '+' o '-'", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la cédula ya está registrada")
    void validarUsuario_CedulaDuplicada() {
        usuarioDtoValido.setCedula(1L);
        usuarioDtoValido.setNombre("Juan");
        usuarioDtoValido.setApellido("Pérez");
        usuarioDtoValido.setEmail("juan@example.com");
        usuarioDtoValido.setRol(Rol.MIEMBRO);
        usuarioDtoValido.setContraseña("abc12345");
        usuarioDtoValido.setTelefono("123456789");
        usuarioDtoValido.setFechaRegistro(LocalDateTime.now(clock));

        when(usuarioDAO.findAll()).thenReturn(List.of(usuarioDtoValido));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.save(usuarioDtoValido));

        assertEquals("El cedula es ya esta registrado", exception.getMessage());
    }


    @Test
    @DisplayName("CREATE - Usuario válido debe retornar usuario creado con ID")
    void createUsuario_DeberiaRetornarUsuarioCreadoConId() {
        //Arrange
        UsuarioDto toCreate = new UsuarioDto();
        toCreate.setCedula(1L);
        toCreate.setNombre("Nombre");
        toCreate.setApellido("Apellido");
        toCreate.setEmail("example@example.com");
        toCreate.setRol(Rol.MIEMBRO);
        toCreate.setContraseña("123456789A");
        toCreate.setFechaRegistro(LocalDateTime.now(clock));
        toCreate.setTelefono("123456789");

        UsuarioDto persisted = new UsuarioDto();
        persisted.setCedula(idValido);
        persisted.setNombre("Nombre");
        persisted.setApellido("Apellido");
        persisted.setEmail("example@example.com");
        persisted.setRol(Rol.MIEMBRO);
        persisted.setFechaRegistro(LocalDateTime.now(clock));
        persisted.setTelefono("123456789");

        when(usuarioDAO.save(any(UsuarioDto.class))).thenReturn(persisted);

        //Act
        UsuarioDto result = usuarioService.save(toCreate);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getCedula()).isEqualTo(idValido);
        assertThat(result.getNombre()).isEqualTo(toCreate.getNombre());

        //Assert - Comportamiento
        ArgumentCaptor<UsuarioDto> captor = ArgumentCaptor.forClass(UsuarioDto.class);
        verify(usuarioDAO, times(1)).save(captor.capture());
        UsuarioDto passed = captor.getValue();
        assertThat(passed.getCedula()).isEqualTo(toCreate.getCedula());
        assertThat(passed.getEmail()).isEqualTo(toCreate.getEmail());
    }

    @Test
    @DisplayName("CREATE - Arroja excepcion al estar duplicado el email")
    void createUsuario_ArrojaExcepcionUsuarioConEmailDuplicado() {
        // Arrange
        UsuarioDto existente = new UsuarioDto();
        existente.setCedula(2L);
        existente.setNombre("Usuario Existente");
        existente.setApellido("Prueba");
        existente.setEmail("example@example.com");
        existente.setRol(Rol.MIEMBRO);
        existente.setContraseña("123456789A");
        existente.setTelefono("987654321");
        existente.setFechaRegistro(LocalDateTime.now(clock));

        when(usuarioDAO.findAll()).thenReturn(List.of(existente));

        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setCedula(3L);
        nuevo.setNombre("Nuevo");
        nuevo.setApellido("Usuario");
        nuevo.setEmail("example@example.com"); // duplicado
        nuevo.setRol(Rol.MIEMBRO);
        nuevo.setContraseña("123456789A");
        nuevo.setTelefono("123456789");
        nuevo.setFechaRegistro(LocalDateTime.now(clock));

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.save(nuevo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email es ya esta registrado");

        verify(usuarioDAO, never()).save(any());
    }

    @Test
    @DisplayName("GET by id - debe retornar usuario")
    void getById_debeRetornarUsuario(){
        //Arrange
        when(usuarioDAO.findById(idValido)).thenReturn(Optional.of(usuarioDtoValido));

        //Act
        UsuarioDto result = usuarioService.findById(idValido);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getCedula()).isEqualTo(idValido);
        assertThat(result.getNombre()).isEqualTo(usuarioDtoValido.getNombre());
        verify(usuarioDAO, times(1)).findById(idValido);
    }

    @Test
    @DisplayName("GET by id - debe arrojar excepcion al no encontrar al usuario")
    void getById_debeArrojarExcepcionUsuario(){
        //Arrange
        when(usuarioDAO.findById(anyLong())).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> usuarioService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        //Verify
        verify(usuarioDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia)")
    void getAll_debeRetornarUnaLista(){
        //Arrange
        when(usuarioDAO.findAll()).thenReturn(List.of(usuarioDtoValido));

        //Act
        List<UsuarioDto> results = usuarioService.findAll();

        //Assert
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        verify(usuarioDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE - elimina un usuario")
    void delete_debeEliminarUsuario(){

        //Arrange
        when(usuarioDAO.findById(idValido)).thenReturn(Optional.of(usuarioDtoValido));
        when(usuarioDAO.delete(idValido)).thenReturn(true);

        // Simulamos que NO hay reservas ni reportes
        when(reservaService.findAll()).thenReturn(Collections.emptyList());
        when(reporteService.findAll()).thenReturn(Collections.emptyList());

        //Act & Assert (no debe lanzar excepcion)
        assertThatCode(() -> usuarioService.deleteUsuario(idValido))
                .doesNotThrowAnyException();

        //Verificacioones
        verify(usuarioDAO, times(1)).findById(idValido);
        verify(usuarioDAO, times(1)).delete(idValido);
    }

    @Test
    @DisplayName("DELETE - debe lanzar excepción si el usuario tiene reservas asociadas")
    void delete_debeFallarSiUsuarioTieneReservas() {
        // Arrange
        when(usuarioDAO.findById(idValido)).thenReturn(Optional.of(usuarioDtoValido));

        // Simulamos que el usuario tiene una reserva
        ReservaDto reservaMock = new ReservaDto();
        reservaMock.setCedula(idValido);
        when(reservaService.findAll()).thenReturn(List.of(reservaMock));

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.deleteUsuario(idValido))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede eliminar el usuario");

        // Verificaciones
        verify(usuarioDAO, times(1)).findById(idValido);
        verify(reservaService, times(1)).findAll();
        verify(reporteService, never()).findAll(); // No llega a revisar reportes
        verify(usuarioDAO, never()).delete(idValido);
    }

    @Test
    @DisplayName("DELETE - debe lanzar excepción si el usuario tiene reportes asociados")
    void delete_debeFallarSiUsuarioTieneReportes() {
        // Arrange
        when(usuarioDAO.findById(idValido)).thenReturn(Optional.of(usuarioDtoValido));

        // No tiene reservas
        when(reservaService.findAll()).thenReturn(Collections.emptyList());

        // Simulamos que tiene un reporte
        ReporteDto reporteMock = new ReporteDto();
        reporteMock.setCedula(idValido);
        when(reporteService.findAll()).thenReturn(List.of(reporteMock));

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.deleteUsuario(idValido))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede eliminar el usuario");

        // Verificaciones
        verify(usuarioDAO, times(1)).findById(idValido);
        verify(reservaService, times(1)).findAll();
        verify(reporteService, times(1)).findAll();
        verify(usuarioDAO, never()).delete(idValido);
    }

    @Test
    @DisplayName("UPDATE - modifica los campos")
    void updateUsuario_modificaLosCampos(){
        //Arrange
        UsuarioDto existing = new UsuarioDto();
        existing.setCedula(idValido);
        existing.setNombre("Antonio");
        existing.setApellido("Buenafuente");
        existing.setEmail("antonio@gmail.com");
        existing.setRol(Rol.VISITANTE);
        existing.setContraseña("12345678A");
        existing.setFechaRegistro(LocalDateTime.now(clock));
        existing.setTelefono("123456789");

        UsuarioDto update = new UsuarioDto();
        update.setNombre("Antonio Nuevo");
        update.setApellido("Buenafuente Nuevo");
        update.setEmail("antonioBuena@gmail.com");
        update.setRol(existing.getRol());
        update.setContraseña(existing.getContraseña());
        update.setFechaRegistro(existing.getFechaRegistro());
        update.setTelefono(existing.getTelefono());

        UsuarioDto updated = new UsuarioDto();
        updated.setCedula(idValido);
        updated.setNombre("Antonio Nuevo");
        updated.setApellido("Buenafuente Nuevo");
        updated.setEmail("antonioBuena@gmail.com");
        updated.setRol(existing.getRol());
        updated.setContraseña(existing.getContraseña());
        updated.setFechaRegistro(existing.getFechaRegistro());
        updated.setTelefono(existing.getTelefono());

        when(usuarioDAO.findById(idValido)).thenReturn(Optional.of(existing));
        when(usuarioDAO.update(eq(idValido), any(UsuarioDto.class))).thenAnswer(invocation -> {
            UsuarioDto passed = invocation.getArgument(1);

            passed.setFechaRegistro(existing.getFechaRegistro());

            return Optional.of(passed);
        });

        //Act
        UsuarioDto result = usuarioService.update(idValido, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Antonio Nuevo");
        assertThat(result.getFechaRegistro()).isEqualTo(existing.getFechaRegistro());

        //Assert - comportamiento
        ArgumentCaptor<UsuarioDto> captor = ArgumentCaptor.forClass(UsuarioDto.class);
        verify(usuarioDAO, times(1)).update(eq(idValido), captor.capture());
        UsuarioDto passed = captor.getValue();
        assertThat(passed.getFechaRegistro()).isEqualTo(existing.getFechaRegistro());
    }

    @Test
    @DisplayName("UPDATE - Arroja RuntimeException si no encuentra al usuario")
    void updateUsuario_arrojaRuntimeException(){
        //Arrange
        when(usuarioDAO.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.update(999L, usuarioDtoValido))
                .isInstanceOf(RuntimeException.class);

        verify(usuarioDAO, times(1)).findById(999L);
        verify(usuarioDAO, never()).update(anyLong(), any());
    }
}
