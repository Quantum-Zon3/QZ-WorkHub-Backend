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
}
