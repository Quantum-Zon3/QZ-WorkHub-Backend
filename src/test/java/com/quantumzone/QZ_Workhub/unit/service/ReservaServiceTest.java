package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.*;
import com.quantumzone.QZ_Workhub.dominio.servicio.*;
import com.quantumzone.QZ_Workhub.persistencia.dao.ReservaDAO;
import com.quantumzone.QZ_Workhub.persistencia.dao.SalaDAO;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - unit Tests")
public class ReservaServiceTest {

    @Mock
    private ReservaDAO reservaDAO;

    @Mock
    private SalaService salaService;

    @Mock
    private Clock clock;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RecursoReservadoService recursoReservadoService;

    @Mock
    private ReporteService reporteService;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private ReservaService reservaService;

    private Long idSalaValida;
    private Long cedulaValida;
    private LocalDateTime ahora;
    private SalaDto salaSimulada;
    private UsuarioDto usuarioSimulado;
    private ReservaDto reservaValida;
    private Long idReserva;
    @BeforeEach
    void setUp() {
        idReserva = 1L;
        idSalaValida = 1L;
        cedulaValida = 12345678L;
        reservaValida = new ReservaDto();



        // Hora fija para pruebas
        ahora = LocalDateTime.of(2025, 1, 10, 10, 0);
        lenient().when(clock.instant())
                .thenReturn(ahora.atZone(ZoneId.systemDefault()).toInstant());

        lenient().when(clock.getZone())
                .thenReturn(ZoneId.systemDefault());

        reservaValida.setIdReserva(idReserva);
        reservaValida.setIdSala(idSalaValida);
        reservaValida.setCedula(cedulaValida);
        reservaValida.setMontoTotal(20000.0);
        reservaValida.setCantidadVisitantes(3);
        reservaValida.setFechaInicio(ahora.plusHours(1));  // 11:00
        reservaValida.setFechaFin(ahora.plusHours(3));
    }

    @Test
    @DisplayName("CREATE - Reserva válida debe guardarse exitosamente")
    void createReserva_DeberiaGuardarExitosamente() {
        // Crear DTO válido
        ReservaDto reservaValida = new ReservaDto();
        reservaValida.setIdReserva(null);
        reservaValida.setIdSala(idSalaValida);
        reservaValida.setCedula(cedulaValida);
        reservaValida.setMontoTotal(20000.0);
        reservaValida.setCantidadVisitantes(3);
        reservaValida.setFechaInicio(ahora.plusHours(1));  // 11:00
        reservaValida.setFechaFin(ahora.plusHours(3));     // 13:00

        //Sala simulada
        SalaDto salaSimulada = new SalaDto();
        salaSimulada.setIdSala(idSalaValida);
        salaSimulada.setCapacidad(5);
        when(salaService.findById(idSalaValida)).thenReturn(salaSimulada);

        //Usuario simulado
        UsuarioDto usuarioSimulado = new UsuarioDto();
        usuarioSimulado.setCedula(cedulaValida);
        when(usuarioService.findById(cedulaValida)).thenReturn(usuarioSimulado);

        //No hay conflictos de horarios
        when(reservaDAO.existeConflicto(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(false);

        //Simular reserva persistida
        ReservaDto reservaPersistida = new ReservaDto();
        reservaPersistida.setIdReserva(10L);
        reservaPersistida.setIdSala(idSalaValida);
        reservaPersistida.setCedula(cedulaValida);
        reservaPersistida.setMontoTotal(20000.0);
        reservaPersistida.setCantidadVisitantes(3);
        reservaPersistida.setFechaInicio(reservaValida.getFechaInicio());
        reservaPersistida.setFechaFin(reservaValida.getFechaFin());

        when(reservaDAO.save(any(ReservaDto.class))).thenReturn(reservaPersistida);

        //Act
        ReservaDto resultado = reservaService.save(reservaValida);

        //Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReserva()).isEqualTo(10L);
        verify(reservaDAO, times(1)).save(any(ReservaDto.class));
    }

    @Test
    @DisplayName("GET by ID - Debe retornar la reserva si existe")
    void findById_DebeRetornarReservaSiExiste() {
        //Arrange
        ReservaDto reserva = new ReservaDto();
        reserva.setIdReserva(1L);

        when(reservaDAO.findById(1L)).thenReturn(Optional.of(reserva));

        //Act
        ReservaDto resultado = reservaService.findById(1L);

        //Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReserva()).isEqualTo(1L);
        verify(reservaDAO, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET by ID - Debe lanzar excepción si no existe la reserva")
    void findById_DebeLanzarExcepcionSiNoExiste() {
        //Arrange
        when(reservaDAO.findById(99L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> reservaService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado la reserva por id: 99");

        verify(reservaDAO, times(1)).findById(99L);
    }

    @Test
    @DisplayName("GET ALL - Debería retornar la lista de reservas")
    void findAll_DeberiaRetornarListaDeReservas() {
        // Arrange
        when(reservaDAO.findAll()).thenReturn(List.of(reservaValida));

        // Act
        List<ReservaDto> resultado = reservaService.findAll();

        // Assert
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.size()).isEqualTo(1);
        verify(reservaDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE - elimina una reserva")
    void deleteReserva_eliminaUnaReserva(){
        when(reservaDAO.findById(idReserva)).thenReturn(Optional.of(reservaValida));
        when(reservaDAO.delete(idReserva)).thenReturn(true);

        assertThatCode(() -> reservaService.deleteReserva(idReserva))
                .doesNotThrowAnyException();

        verify(reservaDAO, times(1)).findById(idReserva);
        verify(reservaDAO, times(1)).delete(idReserva);
    }

    @Test
    @DisplayName("DELETE - arroja RuntimeException no se encontro reserva con id")
    void deleteReserva_arrojaRntime(){
        when(reservaDAO.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.deleteReserva(999L))
                .isInstanceOf(RuntimeException.class);

        verify(reservaDAO, times(1)).findById(999L);
        verify(reservaDAO, never()).delete(anyLong());
    }

    @Test
    @DisplayName("DELETE - lanza excepción si reserva tiene recursos asociados")
    void deleteReserva_conRecursosAsociadosDebeFallar() {
        // Arrange
        when(reservaDAO.findById(idReserva)).thenReturn(Optional.of(reservaValida));

        // Simulamos que hay un recurso relacionado a esta reserva
        RecursoReservadoDto recursoRelacionado = new RecursoReservadoDto();
        recursoRelacionado.setIdReserva(idReserva);

        when(recursoReservadoService.findAll()).thenReturn(List.of(recursoRelacionado));

        // Act & Assert
        assertThatThrownBy(() -> reservaService.deleteReserva(idReserva))
                .isInstanceOf(RuntimeException.class);

        // Verificamos que no siga con los otros servicios ni elimine nada
        verify(reporteService, never()).findAll();
        verify(notificacionService, never()).findAll();
        verify(reservaDAO, never()).delete(anyLong());
    }

    @Test
    @DisplayName("DELETE - lanza excepción si reserva tiene reportes asociados")
    void deleteReserva_conReportesAsociadosDebeFallar() {
        // Arrange
        when(reservaDAO.findById(idReserva)).thenReturn(Optional.of(reservaValida));

        // Simulamos que hay un recurso relacionado a esta reserva
        ReporteDto reporteRelacionado = new ReporteDto();
        reporteRelacionado.setIdReserva(idReserva);

        when(reporteService.findAll()).thenReturn(List.of(reporteRelacionado));

        // Act & Assert
        assertThatThrownBy(() -> reservaService.deleteReserva(idReserva))
                .isInstanceOf(RuntimeException.class);

        // Verificamos que no siga con los otros servicios ni elimine nada
        verify(notificacionService, never()).findAll();
        verify(reservaDAO, never()).delete(anyLong());
    }

    @Test
    @DisplayName("DELETE - lanza excepción si reserva tiene notificiones asociados")
    void deleteReserva_conNotificacionesAsociadosDebeFallar() {
        // Arrange
        when(reservaDAO.findById(idReserva)).thenReturn(Optional.of(reservaValida));

        // Simulamos que hay un recurso relacionado a esta reserva
        NotificacionDto notificacionRelacionado = new NotificacionDto();
        notificacionRelacionado.setIdReserva(idReserva);

        when(notificacionService.findAll()).thenReturn(List.of(notificacionRelacionado));

        // Act & Assert
        assertThatThrownBy(() -> reservaService.deleteReserva(idReserva))
                .isInstanceOf(RuntimeException.class);

        // Verificamos que no siga con los otros servicios ni elimine nada
        verify(reservaDAO, never()).delete(anyLong());
    }

    @Test
    @DisplayName("UPDATE - Debería actualizar la reserva exitosamente")
    void update_DeberiaActualizarReserva() {
        Long id = idReserva;
        // Arrange
        SalaDto salaSimulada = new SalaDto();
        salaSimulada.setIdSala(idSalaValida);
        salaSimulada.setCapacidad(10);
        when(salaService.findById(idSalaValida)).thenReturn(salaSimulada);

        // Simular usuario válido
        UsuarioDto usuarioSimulado = new UsuarioDto();
        usuarioSimulado.setCedula(cedulaValida);
        when(usuarioService.findById(cedulaValida)).thenReturn(usuarioSimulado);

        // mock findById en reservaDAO
        when(reservaDAO.findById(id)).thenReturn(Optional.of(reservaValida));

        ReservaDto reservaActualizada = new ReservaDto();
        reservaActualizada.setIdReserva(id);
        reservaActualizada.setIdSala(idSalaValida);
        reservaActualizada.setCedula(cedulaValida);
        reservaActualizada.setMontoTotal(30000.0);
        reservaActualizada.setCantidadVisitantes(4);
        reservaActualizada.setFechaInicio(ahora.plusHours(2));
        reservaActualizada.setFechaFin(ahora.plusHours(5));

        // findById no lanza excepción
        when(reservaDAO.findById(id)).thenReturn(Optional.of(reservaValida));

        // update devuelve la reserva modificada
        when(reservaDAO.update(id, reservaValida)).thenReturn(Optional.of(reservaActualizada));

        // Act
        ReservaDto resultado = reservaService.update(id, reservaValida);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReserva()).isEqualTo(id);
        assertThat(resultado.getIdSala()).isEqualTo(idReserva);
        verify(reservaDAO).findById(id);
        verify(reservaDAO).update(id, reservaValida);
    }

    @Test
    @DisplayName("UPDATE - arroja runtime si no existe la reserva")
    void updateReserva_arrojaRuntimeException() {
        when(reservaDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.update(999L, reservaValida))
                .isInstanceOf(RuntimeException.class);

        verify(reservaDAO, times(1)).findById(999L);
        verify(reservaDAO, never()).update(anyLong(), any());
    }

}
