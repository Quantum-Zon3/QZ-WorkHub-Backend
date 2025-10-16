package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.enums.EstadoPago;
import com.quantumzone.QZ_Workhub.dominio.enums.MetodoPago;
import com.quantumzone.QZ_Workhub.dominio.servicio.PagoService;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.persistencia.dao.PagoDAO;
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

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - unit Tests")
public class PagoServiceTest {
    // Dependencias Mockeadas
    @Mock
    private PagoDAO pagoDAO;

    @Mock
    private Clock clock;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private PagoService pagoService;

    private PagoDto pagoValido;
    private Long idPagoValido;
    private Long idReservaValida;

    @BeforeEach
    void setUp() {
        idPagoValido = 1L;
        idReservaValida = 1L;
        pagoValido = new PagoDto();

        Instant fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        pagoValido.setIdPago(idPagoValido);
        pagoValido.setMonto(20000.0);
        pagoValido.setFechaRealizacion(LocalDateTime.now(clock));
        pagoValido.setEstadoPago(EstadoPago.COMPLETADO);
        pagoValido.setMetodoPago(MetodoPago.PAYPAL);
        pagoValido.setIdReserva(idReservaValida);
    }
        @Test
        @DisplayName("CREATE - Pago válido debe retornar pago creado exitosamente")
        void createPago_DeberiaRetornarPagoCreado(){
            //ARRANGE (GIVEN) - preparar el escenario
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

            // Arrange
            PagoDto toCreate = new PagoDto();
            toCreate.setMonto(20000.0);
            toCreate.setFechaRealizacion(LocalDateTime.now(clock));
            toCreate.setEstadoPago(EstadoPago.COMPLETADO);
            toCreate.setMetodoPago(MetodoPago.PAYPAL);
            toCreate.setIdReserva(idReservaValida);

            PagoDto persisted = new PagoDto();
            persisted.setIdPago(idPagoValido);
            persisted.setMonto(toCreate.getMonto());
            persisted.setFechaRealizacion(toCreate.getFechaRealizacion());
            persisted.setEstadoPago(toCreate.getEstadoPago());
            persisted.setMetodoPago(toCreate.getMetodoPago());
            persisted.setIdReserva(idReservaValida);

            when(pagoDAO.save(any(PagoDto.class))).thenReturn(persisted);

            // Act
            PagoDto result = pagoService.save(toCreate);

            // Assert - estado
            assertThat(result).isNotNull();
            assertThat(result.getIdPago()).isEqualTo(idPagoValido);
            assertThat(result.getIdReserva()).isEqualTo(idReservaValida);

            // Assert - comportamiento
            ArgumentCaptor<PagoDto> captor = ArgumentCaptor.forClass(PagoDto.class);
            verify(pagoDAO, times(1)).save(captor.capture());
            PagoDto passed = captor.getValue();
            assertThat(passed.getIdPago()).isNull();
            assertThat(passed.getIdReserva()).isEqualTo(idReservaValida);
        }

    @Test
    @DisplayName("GET by id - pago existente retorna DTO")
    void getPagoById_retornaPago(){
        // Arrange
        when(pagoDAO.findById(idPagoValido)).thenReturn(Optional.of(pagoValido));

        // Act & Assert
        PagoDto result = pagoService.findById(idPagoValido);

        assertThat(result).isNotNull();
        assertThat(result.getIdPago()).isEqualTo(idPagoValido);
        assertThat(result.getMetodoPago().compareTo(pagoValido.getMetodoPago()));
        verify(pagoDAO, times(1)).findById(idPagoValido);
    }

    @Test
    @DisplayName("GET by id - pago no existe lanza excepcion")
    void getPagoById_retornaPagoRuntimeException(){
        //Arrange
        when(pagoDAO.findById(anyLong())).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> pagoService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        verify(pagoDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia)")
    void getPagos_retornaUnaLista(){
        //Arrange
        when(pagoDAO.findAll()).thenReturn(List.of(pagoValido));

        List<PagoDto> results = pagoService.findAll();

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(1);
        verify(pagoDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("UPDATE - actualiza los campos")
    void updatePago_actualizaLosCampos(){
        ReservaDto reservaSimulada = new ReservaDto();
        reservaSimulada.setIdReserva(idReservaValida);
        reservaSimulada.setFechaInicio(LocalDateTime.now(clock));
        reservaSimulada.setFechaFin(LocalDateTime.now(clock));
        reservaSimulada.setMontoTotal(10000.0);
        reservaSimulada.setCantidadVisitantes(5);
        reservaSimulada.setCedula(12345678L);
        reservaSimulada.setIdSala(1L);
        reservaSimulada.setIdPago(1L);

        //Arrange
        PagoDto existing = new PagoDto();
        existing.setIdPago(idPagoValido);
        existing.setMonto(20000.0);
        existing.setFechaRealizacion(LocalDateTime.now(clock));
        existing.setEstadoPago(EstadoPago.COMPLETADO);
        existing.setMetodoPago(MetodoPago.PAYPAL);
        existing.setIdReserva(pagoValido.getIdReserva());

        PagoDto update = new PagoDto();
        update.setMonto(existing.getMonto());
        update.setFechaRealizacion(existing.getFechaRealizacion());
        update.setEstadoPago(EstadoPago.COMPLETADO);
        update.setMetodoPago(MetodoPago.EFECTIVO);
        update.setIdReserva(3L);

        PagoDto updated = new PagoDto();
        updated.setIdPago(idPagoValido);
        updated.setMonto(existing.getMonto());
        updated.setFechaRealizacion(existing.getFechaRealizacion());
        updated.setMetodoPago(MetodoPago.EFECTIVO);
        updated.setEstadoPago(existing.getEstadoPago());
        updated.setIdReserva(existing.getIdReserva());

        when(pagoDAO.findById(idPagoValido)).thenReturn(Optional.of(existing));
        when(pagoDAO.update(eq(idPagoValido), any(PagoDto.class))).thenAnswer(invocation -> {
            PagoDto passed = invocation.getArgument(1);

            passed.setIdReserva(existing.getIdReserva());

            return Optional.of(passed);
        });

        //Mockeamos reservaSerive para cualquier id => evitamos excepciones con el validarNotifcación
        when(reservaService.findById(anyLong())).thenReturn(reservaSimulada);

        //Act
        PagoDto result = pagoService.update(idPagoValido, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getMetodoPago().compareTo(MetodoPago.EFECTIVO));
        assertThat(result.getIdReserva()).isEqualTo(existing.getIdReserva());

        //Assert - compartamiento
        ArgumentCaptor<PagoDto> captor = ArgumentCaptor.forClass(PagoDto.class);
        verify(pagoDAO, times(1)).update(eq(idPagoValido), captor.capture());
        PagoDto passed = captor.getValue();
        assertThat(passed.getIdReserva()).isEqualTo(idReservaValida);
    }

    @Test
    @DisplayName("UPDATE - pago no existe")
    void updatePago_retornaPagoRuntimeException(){
        when(pagoDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.update(999L, pagoValido))
                .isInstanceOf(RuntimeException.class);

        verify(pagoDAO, times(1)).findById(999L);
        verify(pagoDAO, never()).update(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE - eliminar el pago")
    void deletePago_eliminarPago(){
        when(pagoDAO.findById(idPagoValido)).thenReturn(Optional.of(pagoValido));
        when(pagoDAO.delete(idPagoValido)).thenReturn(true);

        //Act & Assert
        assertThatCode(() -> pagoService.deletePago(idPagoValido))
                .doesNotThrowAnyException();

        // verificaciones
        verify(pagoDAO, times(1)).findById(idPagoValido);
        verify(pagoDAO, times(1)).delete(idPagoValido);
    }

    @Test
    @DisplayName("DELETE - Arroja runtime si no encuentra un pago")
    void deletePago_arrojaRuntimeException(){
        //Arrange
        when(pagoDAO.findById(999L)).thenReturn(Optional.empty());
        //Assert
        assertThatThrownBy(() -> pagoService.deletePago(999L))
                .isInstanceOf(RuntimeException.class);
        //Verify
        verify(pagoDAO, times(1)).findById(999L);
        verify(pagoDAO, never()).delete(anyLong());
    }
}
