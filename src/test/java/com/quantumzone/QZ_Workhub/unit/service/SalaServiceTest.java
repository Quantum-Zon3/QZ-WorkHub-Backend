package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.dominio.servicio.ReservaService;
import com.quantumzone.QZ_Workhub.dominio.servicio.SalaService;
import com.quantumzone.QZ_Workhub.persistencia.dao.SalaDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalaServices - unit Tests")
public class SalaServiceTest {

    @Mock
    private SalaDAO salaDAO;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private SalaService salaService;



    private Long idValido;
    private SalaDto salaDtoValida;

    @BeforeEach
    void setUp() {
        idValido = 1L;
        salaDtoValida = new SalaDto();
        salaDtoValida.setIdSala(idValido);
        salaDtoValida.setNombre("Sala de Audiovisuales");
        salaDtoValida.setCapacidad(3);
        salaDtoValida.setDescripcion("blablablablebleble");
        salaDtoValida.setPrecio(3000F);
    }

    @Test
    @DisplayName("CREATE - Retorna sala creada con ID")
    void createSala_deberiaRetornarSalaCreadaConId() {
        //Arrange
        SalaDto toCreate = new SalaDto();
        toCreate.setIdSala(3L);
        toCreate.setNombre("Sala de Audiovisuales");
        toCreate.setCapacidad(3);
        toCreate.setDescripcion("blablablablebleble");
        toCreate.setPrecio(3000F);

        SalaDto persisted = new SalaDto();
        persisted.setIdSala(idValido);
        persisted.setNombre("Sala de Audiovisuales");
        persisted.setCapacidad(3);
        persisted.setDescripcion("blablablablebleble");
        persisted.setPrecio(3000F);

        when(salaDAO.save(any(SalaDto.class))).thenReturn(persisted);

        //Act
        SalaDto result = salaService.save(toCreate);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getIdSala()).isEqualTo(idValido);
        assertThat(result.getNombre()).isEqualTo(toCreate.getNombre());

        //Assert - comportamiento
        ArgumentCaptor<SalaDto> captor = ArgumentCaptor.forClass(SalaDto.class);
        verify(salaDAO, times(1)).save(captor.capture());
        SalaDto passed = captor.getValue();
        assertThat(passed.getIdSala()).isEqualTo(toCreate.getIdSala());
        assertThat(passed.getNombre()).isEqualTo(toCreate.getNombre());
    }

    @Test
    @DisplayName("CREATE - Arroja RuntimeException si la id esta duplicada")
    void createSala_arrojaRuntimeException() {
        //Arrange
        SalaDto existente = new SalaDto();
        existente.setIdSala(2L);
        existente.setNombre("Sala de Video");
        existente.setCapacidad(3);
        existente.setDescripcion("wawawawa");
        existente.setPrecio(3000F);

        when(salaDAO.findById(existente.getIdSala())).thenReturn(Optional.of(existente));

        SalaDto nuevo = new SalaDto();
        nuevo.setIdSala(2L);
        nuevo.setNombre("Sala de Audiovisuales");
        nuevo.setCapacidad(3);
        nuevo.setDescripcion("blablablablebleble");
        nuevo.setPrecio(3000F);

        //Act & Assert
        assertThatThrownBy(() -> salaService.save(nuevo))
                .isInstanceOf(IllegalArgumentException.class);

        verify(salaDAO, never()).save(any());
    }

    @Test
    @DisplayName("GET by id - retorna una sala")
    void getById_retornaUnaSala(){
        //Arrange
        when(salaDAO.findById(idValido)).thenReturn(Optional.of(salaDtoValida));

        //Act
        SalaDto result = salaService.findById(idValido);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdSala()).isEqualTo(idValido);
        assertThat(result.getNombre()).isEqualTo(salaDtoValida.getNombre());
        verify(salaDAO, times(1)).findById(idValido);
    }

    @Test
    @DisplayName("GET by id - arroja RuntimeException al no existir la sala")
    void getById_arrojaRuntimeException(){
        //Arrange
        when(salaDAO.findById(999L)).thenReturn(Optional.empty());

        //Assert
        assertThatThrownBy(() -> salaService.findById(999L))
                .isInstanceOf(RuntimeException.class);

        //Verify
        verify(salaDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET all - retorna una lista (no vacia)")
    void getAll_retornaUnaLista(){
        //Arrange
        when(salaDAO.findAll()).thenReturn(List.of(salaDtoValida));

        //Act
        List<SalaDto> result = salaService.findAll();

        //Assert & Verify
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(salaDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE - elimina sala correctamente cuando no hay reservas asociadas")
    void deleteSala_SinReservas_Exito() {
        when(salaDAO.findById(idValido)).thenReturn(Optional.of(salaDtoValida));
        when(salaDAO.delete(idValido)).thenReturn(true);

        when(reservaService.findAll()).thenReturn(Collections.emptyList());

        assertThatCode(() -> salaService.deleteSala(idValido))
                .doesNotThrowAnyException();

        verify(salaDAO, times(1)).findById(idValido);
        verify(salaDAO, times(1)).delete(idValido);
    }

    @Test
    @DisplayName("DELETE - arroja IllegalStateException al tener reservas asociadas")
    void delete_arroja_IllegalStateException(){
        // Simular que la sala existe
        when(salaDAO.findById(idValido)).thenReturn(Optional.of(salaDtoValida));

        // Simular reserva con mismo id (según tu lógica actual)
        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setIdReserva(idValido);
        when(reservaService.findAll()).thenReturn(List.of(reservaDto));

        assertThatThrownBy(() -> salaService.deleteSala(idValido)).isInstanceOf(IllegalStateException.class);

        // Verificar que NO se intentó eliminar la sala
        verify(salaDAO, never()).delete(any());
    }

    @Test
    @DisplayName("UPDATE - modificar campos")
    void update_cambiaCampos(){
        //Arrange
        SalaDto existing = new SalaDto();
        existing.setIdSala(idValido);
        existing.setNombre("Audiovisuales");
        existing.setCapacidad(3);
        existing.setDescripcion("laralalala");
        existing.setPrecio(5000F);

        SalaDto update = new SalaDto();
        update.setIdSala(existing.getIdSala());
        update.setNombre("Tecnológica");
        update.setCapacidad(existing.getCapacidad());
        update.setDescripcion(existing.getDescripcion());
        update.setPrecio(existing.getPrecio());

        SalaDto updated = new SalaDto();
        updated.setIdSala(idValido);
        updated.setNombre("Tecnológica");
        updated.setCapacidad(existing.getCapacidad());
        updated.setDescripcion(existing.getDescripcion());
        updated.setPrecio(existing.getPrecio());

        when(salaDAO.findById(idValido)).thenReturn(Optional.of(existing));
        when(salaDAO.update(eq(idValido), any(SalaDto.class))).thenAnswer(invocation -> {
            SalaDto passed = invocation.getArgument(1);

            passed.setCapacidad(existing.getCapacidad());

            return Optional.of(passed);
        });

        //Act
        SalaDto result = salaService.update(idValido, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Tecnológica");
        assertThat(result.getCapacidad()).isEqualTo(existing.getCapacidad());

        //Assert - comportamiento
        ArgumentCaptor<SalaDto> captor = ArgumentCaptor.forClass(SalaDto.class);
        verify(salaDAO, times(1)).update(eq(idValido), captor.capture());
        SalaDto passed = captor.getValue();
        assertThat(passed.getCapacidad()).isEqualTo(existing.getCapacidad());
    }

    @Test
    @DisplayName("UPDATE - arroja RuntimeException si no existe la sala")
    void updateSala_arroja_RuntimeException(){
        //Arrange
        when(salaDAO.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salaService.update(999L, salaDtoValida))
                .isInstanceOf(RuntimeException.class);

        verify(salaDAO, times(1)).findById(999L);
        verify(salaDAO, never()).update(any(), any());
    }

}
