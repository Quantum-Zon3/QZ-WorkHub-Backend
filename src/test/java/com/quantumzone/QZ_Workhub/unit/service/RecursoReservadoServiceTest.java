package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoReservadoService;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoService;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoReservadoDAO;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
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
@DisplayName("RecursoReservadoService - unit Tests")
public class RecursoReservadoServiceTest {
    @Mock
    private RecursoReservadoDAO recursoReservadoDAO;

    @Mock
    private Clock clock;

    @Mock
    private RecursoService recursoService;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private RecursoReservadoService recursoReservadoService;

    private RecursoReservadoDto recursoRDtoValido;
    private Long idRecursoRValido;
    private Long idReservaValida;
    private Long idRecursoValido;

    @BeforeEach
    void setUp(){
        idRecursoRValido = 1L;
        idRecursoValido = 1L;
        idReservaValida = 1L;

        recursoRDtoValido = new RecursoReservadoDto();

        Instant fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        recursoRDtoValido.setId(idRecursoRValido);
        recursoRDtoValido.setCantidad(2L);
        recursoRDtoValido.setFechaInicio(LocalDateTime.now(clock));
        recursoRDtoValido.setFechaFin(LocalDateTime.now(clock));
        recursoRDtoValido.setMontoTotal(4000);
        recursoRDtoValido.setIdRecurso(idRecursoRValido);
        recursoRDtoValido.setIdReserva(idReservaValida);
    }

    @Test
    @DisplayName("CREATE - recursoR valido debe retornar recursoR + ID")
    void createRecursoRValido_DeberiaRetornarRecursoRCreado(){
        //Arrange
        ReservaDto reservaSimulada = new ReservaDto();
        reservaSimulada.setIdReserva(idReservaValida);
        reservaSimulada.setFechaInicio(LocalDateTime.now(clock));
        reservaSimulada.setFechaFin(LocalDateTime.now(clock));
        reservaSimulada.setMontoTotal(10000.0);
        reservaSimulada.setCantidadVisitantes(5);
        reservaSimulada.setCedula(12345678L);
        reservaSimulada.setIdSala(1L);
        reservaSimulada.setIdPago(1L);

        when(reservaService.findById(idReservaValida)).thenReturn(reservaSimulada);

        RecursoDto recursoSimulado = new RecursoDto();
        recursoSimulado.setIdRecurso(idRecursoValido);
        recursoSimulado.setNombre("Mouse");
        recursoSimulado.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        recursoSimulado.setUnidades(2);
        recursoSimulado.setDescripcion("color negro con leds");
        recursoSimulado.setPrecio(299F);

        when(recursoService.findById(idRecursoValido)).thenReturn(recursoSimulado);

        RecursoReservadoDto toCreate = new RecursoReservadoDto();
        toCreate.setCantidad(2L);
        toCreate.setFechaInicio(LocalDateTime.now(clock));
        toCreate.setFechaFin(LocalDateTime.now(clock));
        toCreate.setMontoTotal(4000);
        toCreate.setIdRecurso(idRecursoRValido);
        toCreate.setIdReserva(idReservaValida);

        RecursoReservadoDto persisted = new RecursoReservadoDto();
        persisted.setId(idRecursoRValido);
        persisted.setCantidad(toCreate.getCantidad());
        persisted.setFechaInicio(toCreate.getFechaInicio());
        persisted.setFechaFin(toCreate.getFechaFin());
        persisted.setMontoTotal(toCreate.getMontoTotal());
        persisted.setIdRecurso(toCreate.getIdRecurso());
        persisted.setIdReserva(toCreate.getIdReserva());

        when(recursoReservadoDAO.save(any(RecursoReservadoDto.class))).thenReturn(persisted);

        RecursoReservadoDto result = recursoReservadoService.save(toCreate);

        assertThat(result).isNotNull();
        assertThat(result.getCantidad()).isEqualTo(toCreate.getCantidad());
        assertThat(result.getId()).isEqualTo(idRecursoRValido);

        ArgumentCaptor<RecursoReservadoDto> captor = ArgumentCaptor.forClass(RecursoReservadoDto.class);
        verify(recursoReservadoDAO, times(1)).save(captor.capture());
        RecursoReservadoDto passed = captor.getValue();
        assertThat(passed.getId()).isNull();
        assertThat(passed.getMontoTotal()).isEqualTo(toCreate.getMontoTotal());

    }

    @Test
    @DisplayName("GET by id - retorna recursoR existe Dto")
    void getRecursoRById_RetornaRecursoRExiste() {
        //Arrange
        when(recursoReservadoDAO.findById(idRecursoRValido)).thenReturn(Optional.of(recursoRDtoValido));

        //Act
        RecursoReservadoDto result = recursoReservadoService.findById(idRecursoRValido);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idRecursoRValido);
        assertThat(result.getMontoTotal()).isEqualTo(recursoRDtoValido.getMontoTotal());
        verify(recursoReservadoDAO, times(1)).findById(idRecursoRValido);
    }

    @Test
    @DisplayName("GET by id - arroja RuntimeException al no encontrar un RecursoR")
    void getRecursoRById_ArrojaNoExiste_RuntimeException() {
        //Arrange
        when(recursoReservadoDAO.findById(anyLong())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> recursoReservadoService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        verify(recursoReservadoDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia)")
    void getRecursoRById_RetornaUnaLista() {
        //Arrange
        when(recursoReservadoDAO.findAll()).thenReturn(List.of(recursoRDtoValido));

        //Act
        List<RecursoReservadoDto> result = recursoReservadoService.findAll();

        //Asserts
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(recursoReservadoDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("UPDATE - actualizaLosCampos")
    void updateRecursoR_actualizaLosCampos(){
        //Arrange
        //Preparamos las entidades simuladas
        ReservaDto reservaSimulada = new ReservaDto();
        reservaSimulada.setIdReserva(idReservaValida);
        reservaSimulada.setFechaInicio(LocalDateTime.now(clock));
        reservaSimulada.setFechaFin(LocalDateTime.now(clock));
        reservaSimulada.setMontoTotal(10000.0);
        reservaSimulada.setCantidadVisitantes(5);
        reservaSimulada.setCedula(12345678L);
        reservaSimulada.setIdSala(1L);
        reservaSimulada.setIdPago(1L);

        RecursoDto recursoSimulado = new RecursoDto();
        recursoSimulado.setIdRecurso(idRecursoValido);
        recursoSimulado.setNombre("Mouse");
        recursoSimulado.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        recursoSimulado.setUnidades(2);
        recursoSimulado.setDescripcion("color negro con leds");
        recursoSimulado.setPrecio(299F);

        RecursoReservadoDto existing = new RecursoReservadoDto();
        existing.setId(idRecursoRValido);
        existing.setCantidad(4L);
        existing.setFechaInicio(LocalDateTime.now(clock));
        existing.setFechaFin(LocalDateTime.now(clock));
        existing.setMontoTotal(4000);
        existing.setIdRecurso(recursoSimulado.getIdRecurso());
        existing.setIdReserva(reservaSimulada.getIdReserva());

        RecursoReservadoDto update = new RecursoReservadoDto();
        update.setCantidad(2L);
        update.setFechaInicio(LocalDateTime.now(clock));
        update.setFechaFin(LocalDateTime.now(clock));
        update.setMontoTotal(6000);
        update.setIdRecurso(3L);
        update.setIdReserva(2L);

        RecursoReservadoDto updated = new RecursoReservadoDto();
        updated.setId(idRecursoRValido);
        updated.setCantidad(2L);
        updated.setFechaInicio(LocalDateTime.now(clock));
        updated.setFechaFin(LocalDateTime.now(clock));
        updated.setMontoTotal(6000);
        updated.setIdRecurso(existing.getIdRecurso());
        updated.setIdReserva(existing.getIdReserva());

        when(recursoReservadoDAO.findById(idRecursoRValido)).thenReturn(Optional.of(existing));
        when(recursoReservadoDAO.update(eq(idRecursoRValido), any(RecursoReservadoDto.class))).thenAnswer(invocation -> {
            RecursoReservadoDto passed = invocation.getArgument(1);

            passed.setIdRecurso(existing.getIdRecurso());
            passed.setIdReserva(existing.getIdReserva());

            return Optional.of(passed);
        });

        //Mockeamos reservaSerive y recursoService para cualquier id => evitamos excepciones con el validarRecursoR
        when(recursoService.findById(anyLong())).thenReturn(recursoSimulado);
        when(reservaService.findById(anyLong())).thenReturn(reservaSimulada);

        //Act
        RecursoReservadoDto result = recursoReservadoService.update(idRecursoRValido, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getCantidad()).isEqualTo(2L);
        assertThat(result.getIdRecurso()).isEqualTo(existing.getIdRecurso());
        assertThat(result.getIdReserva()).isEqualTo(existing.getIdReserva());

        //Assert - comportamiento
        ArgumentCaptor<RecursoReservadoDto> captor = ArgumentCaptor.forClass(RecursoReservadoDto.class);
        verify(recursoReservadoDAO, times(1)).update(eq(idRecursoRValido), captor.capture());
        RecursoReservadoDto passed = captor.getValue();
        assertThat(passed.getIdRecurso()).isEqualTo(idRecursoValido);
        assertThat(passed.getIdReserva()).isEqualTo(idReservaValida);
    }

    @Test
    @DisplayName("UPDATE - Arroja RuntimeException si no existe")
    void update_ArrojaRuntimeException_si_no_existe(){
        //Arrange
        when(recursoReservadoDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoReservadoService.update(999L, recursoRDtoValido))
                .isInstanceOf(RuntimeException.class);

        verify(recursoReservadoDAO, times(1)).findById(999L);
        verify(recursoReservadoDAO, never()).update(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE - elimina una recurso reservado")
    void delete_elimina_una_recurso_reservado(){
        //Arrange
        when(recursoReservadoDAO.findById(idRecursoRValido)).thenReturn(Optional.of(recursoRDtoValido));
        when(recursoReservadoDAO.delete(idRecursoRValido)).thenReturn(true);

        //Act & Assert
        assertThatCode(() -> recursoReservadoService.delete(idRecursoRValido))
                .doesNotThrowAnyException();

        //Verificaciones
        verify(recursoReservadoDAO, times(1)).findById(idRecursoRValido);
        verify(recursoReservadoDAO, times(1)).delete(idRecursoRValido);
    }

    @Test
    @DisplayName("DELETE - Arroja RuntimeException si no encuentra el recursoR")
    void delete_arroja_runtimeException_si_no_encuentra_el_recursoR(){
        //Arrange
        when(recursoReservadoDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoReservadoService.delete(999L))
                .isInstanceOf(RuntimeException.class);

        verify(recursoReservadoDAO, times(1)).findById(999L);
        verify(recursoReservadoDAO, never()).delete(anyLong());
    }



}
