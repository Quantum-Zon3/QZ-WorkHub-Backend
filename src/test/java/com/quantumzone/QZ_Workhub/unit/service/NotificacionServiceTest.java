package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.NotificacionService;
import com.quantumzone.QZ_Workhub.persistencia.dao.NotificacionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - unit Tests")
public class NotificacionServiceTest {
    // Dependencias Mockeadas
    @Mock
    private NotificacionDAO notificacionDAO;

    //Clase bajo prueba (System under test)
    @InjectMocks
    private NotificacionService notificacionService;

    //Datos de prueba
    private NotificacionDto notificacionValida;
    private Long idNotificacionValida;
    private Long idReservaValida;

    @BeforeEach
    void setUp() {
        idNotificacionValida = 1L;
        idReservaValida = 1L;
        notificacionValida = new NotificacionDto();
            notificacionValida.setIdNotificacion(idNotificacionValida);
            notificacionValida.setMotivo("motivo");
            notificacionValida.setFecha(LocalDateTime.now());
            notificacionValida.setDescripcion("descripcion");
            notificacionValida.setIdReserva(idReservaValida);
    }

    @Test
    @DisplayName("CREATE - Notificacion válido debe retornar notificacion creada con ID")
    void createNotificacion_DeberiaRetornarProductoCreado(){
        //ARRANGE (GIVEN) - preparar el escenario
        // Arrange
        NotificacionDto toCreate = new NotificacionDto();
        toCreate.setIdNotificacion(idNotificacionValida);
        toCreate.setMotivo("Carlos Perez");
        toCreate.setFecha(LocalDateTime.now());
        toCreate.setDescripcion("Descripcion");
        toCreate.setIdReserva(idReservaValida);

        NotificacionDto persisted = new NotificacionDto();
        persisted.setIdNotificacion(idNotificacionValida);
        persisted.setMotivo(toCreate.getMotivo());
        persisted.setFecha(toCreate.getFecha());
        persisted.setDescripcion(toCreate.getDescripcion());

        when(notificacionDAO.save(any(NotificacionDto.class))).thenReturn(persisted);

        // Act
        NotificacionDto result = notificacionService.save(toCreate);

        // Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getIdNotificacion()).isEqualTo(idNotificacionValida);
        assertThat(result.getMotivo()).isEqualTo(toCreate.getMotivo());

        // Assert - comportamiento
        verify(notificacionDAO, times(1)).findById(idNotificacionValida);
        ArgumentCaptor<NotificacionDto> captor = ArgumentCaptor.forClass(NotificacionDto.class);
        verify(notificacionDAO, times(1)).save(captor.capture());
        NotificacionDto passed = captor.getValue();
        assertThat(passed.getIdNotificacion()).isNull();
        assertThat(passed.getMotivo()).isEqualTo(toCreate.getMotivo());
    }

}
