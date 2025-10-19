package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.enums.MotivoReporte;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReporteService;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.dominio.servicio.UsuarioService;
import com.quantumzone.QZ_Workhub.persistencia.dao.ReporteDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReporteService - unit Tests")
public class ReporteServiceTest {

    @Mock
    private ReporteDAO reporteDAO;

    @Mock
    private Clock clock;

    @Mock
    private ReservaService reservaService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ReporteService reporteService;

    private ReporteDto reporteValido;
    private Long idReporte;
    private Long idUsuario;
    private Long idReserva;

    @BeforeEach
    void setUp(){
        idReporte = 1L;
        idUsuario = 1L;
        idReserva = 1L;

        reporteValido = new ReporteDto();

        Instant fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        reporteValido.setIdReporte(idReporte);
        reporteValido.setMotivo(MotivoReporte.INGRESO_VISITANTE);
        reporteValido.setFecha(LocalDateTime.now(clock));
        reporteValido.setCedula(idUsuario);
        reporteValido.setIdReserva(idReserva);
    }

    @Test
    @DisplayName("CREATE - Reporte válido debe retornar reporte creado con ID")
    void createReporte_deberiaRetornarReporteCreadoConId() {
        // Arrange
        // Simulamos usuario existente
        UsuarioDto usuarioSimulado = new UsuarioDto();
        usuarioSimulado.setCedula(idUsuario);
        when(usuarioService.findById(idUsuario)).thenReturn(usuarioSimulado);

        // Simulamos reserva existente
        ReservaDto reservaSimulada = new ReservaDto();
        reservaSimulada.setIdReserva(idReserva);
        when(reservaService.findById(idReserva)).thenReturn(reservaSimulada);

        ReporteDto reporteACrear = new ReporteDto();
        reporteACrear.setMotivo(reporteValido.getMotivo());
        reporteACrear.setFecha(reporteValido.getFecha());
        reporteACrear.setCedula(reporteValido.getCedula());
        reporteACrear.setIdReserva(reporteValido.getIdReserva());

        // Simular respuesta del DAO al guardar
        ReporteDto reportePersistido = new ReporteDto();
        reportePersistido.setIdReporte(idReporte);
        reportePersistido.setMotivo(reporteACrear.getMotivo());
        reportePersistido.setFecha(reporteACrear.getFecha());
        reportePersistido.setCedula(reporteACrear.getCedula());
        reportePersistido.setIdReserva(reporteACrear.getIdReserva());

        when(reporteDAO.save(any(ReporteDto.class))).thenReturn(reportePersistido);

        // Act
        ReporteDto resultado = reporteService.save(reporteACrear);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(idReporte);
        verify(reporteDAO, times(1)).save(any(ReporteDto.class));
    }

    @Test
    @DisplayName("GET by ID - debera retornar un reporte")
    void getById_deberaRetornarUnReporte() {
        //Arrange
        when(reporteDAO.findById(idReporte)).thenReturn(Optional.of(reporteValido));
        //Act
        ReporteDto result = reporteService.findById(idReporte);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdReporte()).isEqualTo(idReporte);
        assertThat(result.getMotivo()).isEqualTo(reporteValido.getMotivo());
        verify(reporteDAO, times(1)).findById(idReporte);
    }

    @Test
    @DisplayName("GET by id - arroja excepcion al no existir")
    void getById_deberaExcepcionAlNoExistir() {
        when(reporteDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        verify(reporteDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia)")
    void getAll_deberaRetornarUnaLista() {
        when(reporteDAO.findAll()).thenReturn(List.of(reporteValido));

        List<ReporteDto> result = reporteService.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(reporteDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE - elimina un reporte")
    void deleteReporte_eliminaUnReporte() {
        when(reporteDAO.findById(idReporte)).thenReturn(Optional.of(reporteValido));
        when(reporteDAO.delete(idReporte)).thenReturn(true);

        assertThatCode(() -> reporteService.deleteReporte(idReporte))
                .doesNotThrowAnyException();

        verify(reporteDAO, times(1)).findById(idReporte);
        verify(reporteDAO, times(1)).delete(idReporte);
    }

    @Test
    @DisplayName("DELETE - arroja runtimeexcepction si no encuentra el reporte")
    void deleteReporte_arrojaRuntimeExcepccionSi() {
        when(reporteDAO.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.deleteReporte(999L))
                .isInstanceOf(RuntimeException.class);

        verify(reporteDAO, times(1)).findById(999L);
        verify(reporteDAO, never()).delete(anyLong());
    }

    @Test
    @DisplayName("UPDATE - actualiza un reporte exitosamente")
    void updateReporte_actualizaExitosamente() {
        // Arrange
        when(reporteDAO.findById(idReporte)).thenReturn(Optional.of(reporteValido));
        when(reservaService.findById(reporteValido.getIdReserva())).thenReturn(new ReservaDto());
        when(usuarioService.findById(reporteValido.getCedula())).thenReturn(new UsuarioDto());
        when(reporteDAO.update(idReporte, reporteValido)).thenReturn(Optional.of(reporteValido));

        // Act
        ReporteDto resultado = reporteService.update(idReporte, reporteValido);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(idReporte);

        verify(reporteDAO, times(1)).findById(idReporte);
        verify(reservaService, times(1)).findById(reporteValido.getIdReserva());
        verify(usuarioService, times(1)).findById(reporteValido.getCedula());
        verify(reporteDAO, times(1)).update(idReporte, reporteValido);
    }

    @Test
    @DisplayName("UPDATE - arroja runtimeException si no encuentra el id")
    void updateReporte_arrojaRuntimeExceptionSi() {
        when(reporteDAO.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.update(999L, reporteValido))
                .isInstanceOf(RuntimeException.class);

        verify(reporteDAO, times(1)).findById(999L);
        verify(reporteDAO, never()).update(anyLong(), any());
    }
}
