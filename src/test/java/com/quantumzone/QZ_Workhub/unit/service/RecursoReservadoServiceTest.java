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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
    private Instant fixedInstant;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp(){
        idRecursoRValido = 1L;
        idRecursoValido = 1L;
        idReservaValida = 1L;


        recursoRDtoValido = new RecursoReservadoDto();

        fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        fixedDateTime = LocalDateTime.now(clock);

        recursoRDtoValido.setId(idRecursoRValido);
        recursoRDtoValido.setCantidad(2L);
        recursoRDtoValido.setFechaInicio(fixedDateTime.minusDays(2));
        recursoRDtoValido.setFechaFin(fixedDateTime.minusDays(1));
        recursoRDtoValido.setMontoTotal(4000);
        recursoRDtoValido.setIdRecurso(idRecursoRValido);
        recursoRDtoValido.setIdReserva(idReservaValida);
    }

    @Test
    @DisplayName("validarRecursoR - Debe lanzar excepción cuando el recurso no existe")
    void validarRecursoR_RecursoNoEncontrado_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = crearDto(999L, 1L, 2L, fixedDateTime,
                fixedDateTime.plusDays(5), 4000);

        when(recursoService.findById(999L)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("Recurso no encontrado", exception.getMessage());
        verify(recursoService).findById(999L);
    }

    @Test
    @DisplayName("validarRecursoR - Debe lanzar excepción cuando la reserva no existe")
    void validarRecursoR_ReservaNoEncontrada_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = crearDto(1L, 999L, 2L, fixedDateTime,
                fixedDateTime.plusDays(5), 4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(999L)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("Reserva no encontrada", exception.getMessage());
        verify(recursoService).findById(1L);
        verify(reservaService).findById(999L);
    }

    @Test
    @DisplayName("validarRecursoR - Debe lanzar excepción cuando las fechas no coinciden con la reserva")
    void validarRecursoR_FechasNoCoinciden_ThrowsException() {
        // Arrange
        LocalDateTime fechaInicioDto = fixedDateTime;
        LocalDateTime fechaFinDto = fixedDateTime.plusDays(5);
        LocalDateTime fechaInicioReserva = fixedDateTime.plusHours(2); // Diferente
        LocalDateTime fechaFinReserva = fixedDateTime.plusDays(10); // Diferente

        RecursoReservadoDto dto = crearDto(1L, 1L, 2L, fechaInicioDto, fechaFinDto, 4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fechaInicioReserva, fechaFinReserva);

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("Las fechas del recurso deben coincidir con las fechas de la reserva",
                exception.getMessage());
        verify(recursoService).findById(1L);
        verify(reservaService).findById(1L);
    }

    @Test
    @DisplayName("validarRecursoR - Debe lanzar excepción cuando no hay suficientes unidades disponibles")
    void validarRecursoR_UnidadesInsuficientes_ThrowsException() {
        // Arrange
        LocalDateTime fechaInicio = fixedDateTime;
        LocalDateTime fechaFin = fixedDateTime.plusDays(5);

        // Solicitar 8 unidades
        RecursoReservadoDto dtoNuevo = crearDto(1L, 1L, 8L, fechaInicio, fechaFin, 4000);
        dtoNuevo.setId(null); // Nueva reserva

        // Recurso con 10 unidades totales
        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fechaInicio, fechaFin);

        // Ya hay una reserva existente de 5 unidades que se solapa
        RecursoReservadoDto reservaExistente = crearDto(1L, 2L, 5L, fechaInicio, fechaFin, 2000);
        reservaExistente.setId(2L);

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);
        when(recursoReservadoDAO.findAll()).thenReturn(Arrays.asList(reservaExistente));

        // Act & Assert
        // Disponibles: 10 - 5 = 5, pero solicita 8
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dtoNuevo)
        );

        assertTrue(exception.getMessage().contains("No hay suficientes unidades disponibles"),
                "El mensaje debe indicar falta de disponibilidad");
        assertTrue(exception.getMessage().contains("Solicitadas: 8"),
                "El mensaje debe mostrar las unidades solicitadas");
        assertTrue(exception.getMessage().contains("Disponibles: 5"),
                "El mensaje debe mostrar las unidades disponibles");

        verify(recursoService).findById(1L);
        verify(reservaService).findById(1L);
        verify(recursoReservadoDAO).findAll();
    }

    @Test
    @DisplayName("validarRecursoR - Debe permitir actualizar una reserva existente sin contar sus propias unidades")
    void validarRecursoR_ActualizarReservaExistente_Success() {
        // Arrange
        LocalDateTime fechaInicio = fixedDateTime;
        LocalDateTime fechaFin = fixedDateTime.plusDays(5);

        // Actualizar reserva existente (ID = 1) de 2 a 8 unidades
        RecursoReservadoDto dtoActualizar = crearDto(1L, 1L, 8L, fechaInicio, fechaFin, 4000);
        dtoActualizar.setId(1L); // Actualización

        // Recurso con 10 unidades totales
        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fechaInicio, fechaFin);

        // La reserva existente es la misma que se está actualizando (ID = 1)
        RecursoReservadoDto reservaExistente = crearDto(1L, 1L, 2L, fechaInicio, fechaFin, 1000);
        reservaExistente.setId(1L);

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);
        when(recursoReservadoDAO.findAll()).thenReturn(Arrays.asList(reservaExistente));

        // Act & Assert
        // No debe contar las 2 unidades de la reserva existente porque es la misma que se actualiza
        // Disponibles: 10 - 0 = 10, solicita 8 -> OK
        assertDoesNotThrow(() -> recursoReservadoService.validarRecursoR(dtoActualizar),
                "Debe permitir actualizar la reserva sin contar sus propias unidades");

        verify(recursoService).findById(1L);
        verify(reservaService).findById(1L);
        verify(recursoReservadoDAO).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha de inicio es null")
    void validarRecursoR_FechaInicioNull_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(2L);
        dto.setFechaInicio(null);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La fecha de inicio es obligatoria", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha de inicio está en el futuro")
    void validarRecursoR_FechaInicioFutura_ThrowsException() {
        // Arrange
        LocalDateTime fechaFutura = fixedDateTime.plusMonths(3);

        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(2L);
        dto.setFechaInicio(fechaFutura);
        dto.setFechaFin(fechaFutura.plusDays(5));
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fechaFutura, fechaFutura.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La fecha de inicio no puede ser en el futuro", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el monto total es null")
    void validarRecursoR_MontoTotalNull_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(2L);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(null);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);
        when(recursoReservadoDAO.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("El monto total es obligatorio y debe ser mayor o igual a cero",
                exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el monto total es negativo")
    void validarRecursoR_MontoTotalNegativo_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(2L);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(-100);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);
        when(recursoReservadoDAO.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("El monto total es obligatorio y debe ser mayor o igual a cero",
                exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cantidad es null")
    void validarRecursoR_CantidadNull_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(null);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La cantidad es obligatoria y debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cantidad es cero")
    void validarRecursoR_CantidadCero_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(0L);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La cantidad es obligatoria y debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cantidad es negativa")
    void validarRecursoR_CantidadNegativa_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(-5L);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(fixedDateTime.plusDays(5));
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La cantidad es obligatoria y debe ser mayor a cero", exception.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha de fin es null")
    void validarRecursoR_FechaFinNull_ThrowsException() {
        // Arrange
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(1L);
        dto.setIdReserva(1L);
        dto.setCantidad(2L);
        dto.setFechaInicio(fixedDateTime);
        dto.setFechaFin(null);
        dto.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fixedDateTime, fixedDateTime.plusDays(5));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recursoReservadoService.validarRecursoR(dto)
        );

        assertEquals("La fecha de finalización es obligatoria", exception.getMessage());
    }

    @Test
    @DisplayName("Debe validar correctamente cuando no hay solapamiento de fechas")
    void validarRecursoR_SinSolapamientoFechas_Success() {
        // Arrange
        // ✅ Usamos fechas ANTES del fixedDateTime para que no falle por "futuro"
        LocalDateTime fechaInicio = fixedDateTime.minusDays(10);
        LocalDateTime fechaFin = fixedDateTime.minusDays(5);

        RecursoReservadoDto dtoNuevo = new RecursoReservadoDto();
        dtoNuevo.setId(null);
        dtoNuevo.setIdRecurso(1L);
        dtoNuevo.setIdReserva(1L);
        dtoNuevo.setCantidad(8L);
        dtoNuevo.setFechaInicio(fechaInicio);
        dtoNuevo.setFechaFin(fechaFin);
        dtoNuevo.setMontoTotal(4000);

        RecursoDto recurso = crearRecursoDto(1L, "Sala Conferencias", 10);
        ReservaDto reserva = crearReservaDto(1L, fechaInicio, fechaFin);

        RecursoReservadoDto reservaExistente = new RecursoReservadoDto();
        reservaExistente.setId(2L);
        reservaExistente.setIdRecurso(1L);
        reservaExistente.setIdReserva(2L);
        reservaExistente.setCantidad(8L);
        reservaExistente.setFechaInicio(fixedDateTime.minusDays(20));
        reservaExistente.setFechaFin(fixedDateTime.minusDays(15));

        when(recursoService.findById(1L)).thenReturn(recurso);
        when(reservaService.findById(1L)).thenReturn(reserva);

        // Act & Assert
        assertDoesNotThrow(() -> recursoReservadoService.validarRecursoR(dtoNuevo));
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

    private static RecursoReservadoDto crearDto(Long idRecurso, Long idReserva,
                                                Long cantidad, LocalDateTime fechaInicio,
                                                LocalDateTime fechaFin, Integer montoTotal) {
        RecursoReservadoDto dto = new RecursoReservadoDto();
        dto.setIdRecurso(idRecurso);
        dto.setIdReserva(idReserva);
        dto.setCantidad(cantidad);
        dto.setFechaInicio(fechaInicio);
        dto.setFechaFin(fechaFin);
        dto.setMontoTotal(montoTotal);
        return dto;
    }

    private RecursoDto crearRecursoDto(Long id, String nombre, Integer unidades) {
        RecursoDto dto = new RecursoDto();
        dto.setIdRecurso(id);
        dto.setNombre(nombre);
        dto.setUnidades(unidades);
        return dto;
    }

    private ReservaDto crearReservaDto(Long id, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        ReservaDto dto = new ReservaDto();
        dto.setIdReserva(id);
        dto.setFechaInicio(fechaInicio);
        dto.setFechaFin(fechaFin);
        return dto;
    }
}
