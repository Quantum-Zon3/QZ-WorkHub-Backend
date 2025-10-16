package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.NotificacionService;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.persistencia.dao.NotificacionDAO;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - unit Tests")
public class NotificacionServiceTest {
    // Dependencias Mockeadas
    @Mock
    private NotificacionDAO notificacionDAO;

    @Mock
    private Clock clock;

    @Mock
    private ReservaService reservaService;

    //Clase bajo prueba (System under test)
    @InjectMocks
    private NotificacionService notificacionService;

    //Datos de prueba
    private NotificacionDto notificacionValida; //NotificacionDto que sera usada como base
    private Long idNotificacionValida; //Id de la notificacion para poder encontrarla
    private Long idReservaValida; //Id de una reserva para poder crear Notificacion

    /**
     * METODO QUE SE EJECUTA ANTES DE CADA TEST CREA OBJETOS REUTILIZABLES
     */
    @BeforeEach
    void setUp() {
        idNotificacionValida = 1L;
        idReservaValida = 1L;
        notificacionValida = new NotificacionDto();

        Instant fixedInstant = Instant.parse("2025-10-08T10:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        /**
         * Setteamos los datos de pueba que son necesarios
         */
        notificacionValida.setIdNotificacion(idNotificacionValida);
        notificacionValida.setMotivo("motivo");
        notificacionValida.setFecha(LocalDateTime.now(clock));
        notificacionValida.setDescripcion("descripcion");
        notificacionValida.setIdReserva(idReservaValida);
    }

    //---------- CREATE ----------

    @Test
    @DisplayName("CREATE - Notificacion válido debe retornar notificacion creada con ID")
    void createNotificacion_DeberiaRetornarNotificacionCreada(){
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
        NotificacionDto toCreate = new NotificacionDto();
        toCreate.setMotivo("Carlos Perez");
        toCreate.setDescripcion("Descripcion");
        toCreate.setFecha(LocalDateTime.now(clock));
        toCreate.setIdReserva(idReservaValida);

        NotificacionDto persisted = new NotificacionDto();
        persisted.setIdNotificacion(idNotificacionValida);
        persisted.setMotivo(toCreate.getMotivo());
        persisted.setFecha(toCreate.getFecha());
        persisted.setDescripcion(toCreate.getDescripcion());
        persisted.setIdReserva(toCreate.getIdReserva());

        when(notificacionDAO.save(any(NotificacionDto.class))).thenReturn(persisted);

        // Act
        NotificacionDto result = notificacionService.save(toCreate);

        // Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getIdNotificacion()).isEqualTo(idNotificacionValida);
        assertThat(result.getMotivo()).isEqualTo(toCreate.getMotivo());

        // Assert - comportamiento
        ArgumentCaptor<NotificacionDto> captor = ArgumentCaptor.forClass(NotificacionDto.class);
        verify(notificacionDAO, times(1)).save(captor.capture());
        NotificacionDto passed = captor.getValue();
        assertThat(passed.getIdNotificacion()).isNull();
        assertThat(passed.getMotivo()).isEqualTo(toCreate.getMotivo());
    }

    //---------- READ ----------

    @Test
    @DisplayName("GET by id - notificacion existente retorna DTO")
    void getNotificacionById_retornaNotificacion(){
        // Arrange
        when(notificacionDAO.findById(idNotificacionValida)).thenReturn(Optional.of(notificacionValida));

        // Act
        NotificacionDto result = notificacionService.findById(idNotificacionValida);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdNotificacion()).isEqualTo(idNotificacionValida);
        assertThat(result.getMotivo()).isEqualTo(notificacionValida.getMotivo());
        verify(notificacionDAO, times(1)).findById(idNotificacionValida);
    }

    @Test
    @DisplayName("GET by id - notificaion no existe lanza RuntimeException")
    void getNotificacionById_lanzaRuntimeException(){
        //Arrange
        when(notificacionDAO.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificacionService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        verify(notificacionDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia")
    void getNotificaciones_retornaUnaLista(){
        // Arrage
        when(notificacionDAO.findAll()).thenReturn(List.of(notificacionValida));

        // Act
        List<NotificacionDto> results = notificacionService.findAll();

        // Assets
        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        verify(notificacionDAO, times(1)).findAll();
    }

    //---------- UPDATE ----------

    @Test
    @DisplayName("UPDATE - actualiza los campos")
    void updateNotificacion_actualizaLosCampos(){
        //Preparamos la reserva simulada
        ReservaDto reservaSimulada = new ReservaDto();
        reservaSimulada.setIdReserva(idReservaValida);
        reservaSimulada.setFechaInicio(LocalDateTime.now(clock));
        reservaSimulada.setFechaFin(LocalDateTime.now(clock));
        reservaSimulada.setMontoTotal(10000.0);
        reservaSimulada.setCantidadVisitantes(5);
        reservaSimulada.setCedula(12345678L);
        reservaSimulada.setIdSala(1L);
        reservaSimulada.setIdPago(1L);

        // Arange

        NotificacionDto existing = new NotificacionDto();
        existing.setIdNotificacion(idNotificacionValida);
        existing.setMotivo("Motivo Viejo");
        existing.setFecha(LocalDateTime.now(clock));
        existing.setDescripcion("Descripcion Vieja");
        existing.setIdReserva(reservaSimulada.getIdReserva());

        NotificacionDto update = new NotificacionDto();
        update.setMotivo("Motivo nuevo");
        update.setDescripcion("Descripcion Nuevo");
        update.setFecha(LocalDateTime.now(clock));
        update.setIdReserva(3L);

        NotificacionDto updated = new NotificacionDto();
        updated.setIdNotificacion(idNotificacionValida);
        updated.setMotivo("Motivo nuevo");
        updated.setFecha(existing.getFecha());
        updated.setDescripcion(existing.getDescripcion());
        updated.setIdReserva(existing.getIdReserva());

        when(notificacionDAO.findById(idNotificacionValida)).thenReturn(Optional.of(existing));
        when(notificacionDAO.update(eq(idNotificacionValida), any(NotificacionDto.class))).thenAnswer(invocation -> {
            NotificacionDto passed = invocation.getArgument(1);

            passed.setIdReserva(existing.getIdReserva());

            return Optional.of(passed);
        });

        //Mockeamos reservaSerive para cualquier id => evitamos excepciones con el validarNotifcación
        when(reservaService.findById(anyLong())).thenReturn(reservaSimulada);

        //Act
        NotificacionDto result = notificacionService.updateNotificacion(idNotificacionValida, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getMotivo()).isEqualTo("Motivo nuevo");
        assertThat(result.getIdReserva()).isEqualTo(existing.getIdReserva());

        //Assert - comportamiento
        ArgumentCaptor<NotificacionDto> captor = ArgumentCaptor.forClass(NotificacionDto.class);
        verify(notificacionDAO, times(1)).update(eq(idNotificacionValida), captor.capture());
        NotificacionDto passed = captor.getValue();
        assertThat(passed.getIdReserva()).isEqualTo(idReservaValida);
    }

    @Test
    @DisplayName("UPDATE - notificacion no existe")
    void updateNotificacion_throwsExceptionSiNoExiste(){
        // Arrage
        when(notificacionDAO.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificacionService.updateNotificacion(999L, notificacionValida))
                .isInstanceOf(RuntimeException.class);

        verify(notificacionDAO, times(1)).findById(999L);
        verify(notificacionDAO, never()).update(anyLong(), any());
    }

    //---------- DELETE ----------

    @Test
    @DisplayName("DELETE - elimina la notificacion si existe")
    void deleteNotificacion_eliminaLaNotificacionSiExiste(){
        // Arrage
        // Importante: notificacionService primero llama a getNotificacionById(id) -> debemos stubear findById
        when(notificacionDAO.findById(idNotificacionValida)).thenReturn(Optional.of(notificacionValida));
        when(notificacionDAO.delete(idNotificacionValida)).thenReturn(true);

        // Act & Assert (no debe lanzar excepcion)
        assertThatCode(() -> notificacionService.deleteNotificacion(idNotificacionValida))
                .doesNotThrowAnyException();

        // Verificaciones
        verify(notificacionDAO, times(1)).findById(idNotificacionValida);
        verify(notificacionDAO, times(1)).delete(idNotificacionValida);
    }

    @Test
    @DisplayName("DELETE - Arroja una RuntimeExcepcion si la notificacion no existe")
    void deleteNotificacion_throwsExceptionSiLaNotificacionNoExiste(){
        // Arange
        when(notificacionDAO.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificacionService.deleteNotificacion(999L))
                .isInstanceOf(RuntimeException.class);

        // Verificaciones
        verify(notificacionDAO, times(1)).findById(999L);
        verify(notificacionDAO, never()).delete(anyLong());
    }
}
