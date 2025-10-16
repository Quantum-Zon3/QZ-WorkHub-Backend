package com.quantumzone.QZ_Workhub.unit.service;

import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoReservadoService;
import com.quantumzone.QZ_Workhub.dominio.servicio.RecursoService;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoDAO;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoReservadoDAO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecursoService - unit Tests")
public class RecursoServiceTest {
    //Dependencias mockeadas
    @Mock
    private RecursoDAO recursoDAO;

    @Mock
    private RecursoReservadoService recursoReservadoService;

    @InjectMocks
    private RecursoService recursoService;

    private RecursoDto recursoDtoValido;
    private Long idRecursoValido;

    @BeforeEach
    void setUp() {
        idRecursoValido = 1L;
        recursoDtoValido = new RecursoDto();

        recursoDtoValido.setIdRecurso(idRecursoValido);
        recursoDtoValido.setNombre("Audifonos");
        recursoDtoValido.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        recursoDtoValido.setUnidades(3);
        recursoDtoValido.setDescripcion("Color negro, cable de fibra de vidrio");
        recursoDtoValido.setPrecio(5000F);


    }

    @Test
    @DisplayName("CREATE - Recurso valido deberia retornar creado con id")
    void createRecurso_deberiaRetornarCreadoConId() {
        //Arrange
        RecursoDto toCreate = new RecursoDto();
        toCreate.setNombre("Wakawaka");
        toCreate.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        toCreate.setUnidades(3);
        toCreate.setDescripcion("Hehesaminamina");
        toCreate.setPrecio(5000F);

        RecursoDto persisted = new RecursoDto();
        persisted.setIdRecurso(idRecursoValido);
        persisted.setNombre(toCreate.getNombre());
        persisted.setTipoRecurso(toCreate.getTipoRecurso());
        persisted.setUnidades(toCreate.getUnidades());
        persisted.setDescripcion(toCreate.getDescripcion());
        persisted.setPrecio(toCreate.getPrecio());

        when(recursoDAO.save(any(RecursoDto.class))).thenReturn(persisted);

        //Act
        RecursoDto result = recursoService.save(toCreate);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getIdRecurso()).isEqualTo(idRecursoValido);
        assertThat(result.getNombre()).isEqualTo(toCreate.getNombre());

        //Assert - comportamiento
        ArgumentCaptor<RecursoDto> captor = ArgumentCaptor.forClass(RecursoDto.class);
        verify(recursoDAO, times(1)).save(captor.capture());
        RecursoDto passed = captor.getValue();
        assertThat(passed.getIdRecurso()).isNull();
        assertThat(passed.getNombre()).isEqualTo(toCreate.getNombre());
    }

    //------ READ --------
    @Test
    @DisplayName("GET by id - recurso existe retorna ID")
    void getById_retornaRecursoExiste() {
        //Arrange
        when(recursoDAO.findById(idRecursoValido)).thenReturn(Optional.of(recursoDtoValido));

        //Act
        RecursoDto result = recursoService.findById(idRecursoValido);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdRecurso()).isEqualTo(idRecursoValido);
        assertThat(result.getNombre()).isEqualTo(recursoDtoValido.getNombre());
        verify(recursoDAO, times(1)).findById(idRecursoValido);
    }

    @Test
    @DisplayName("GET by id - recurso no existe tira RuntimeException")
    void getById_retornaRecursoNoExisteRuntimeException() {
        //Arrange
        when(recursoDAO.findById(anyLong())).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> recursoService.findById(999L))
                .isInstanceOf(RuntimeException.class);
        verify(recursoDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET by tipoRecurso - retornaRecusosConTipo")
    void getById_retornaRecusosConTipo() {
        //
        when(recursoDAO.findByTipoRecurso(TipoRecurso.TECNOLOGICO))
                .thenReturn(Optional.of(List.of(recursoDtoValido)));
        //Act
        List<RecursoDto> result = recursoService.findByTipoRecurso(TipoRecurso.TECNOLOGICO);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(recursoDAO, times(1)).findByTipoRecurso(TipoRecurso.TECNOLOGICO);
    }

    @Test
    @DisplayName("GET by tipoRecurso - arrojaRuntimeException si no hay recursos con ese TipoRecurso")
    void getById_retornaRecusosConTipoRuntimeException() {
        TipoRecurso tipoRecurso = TipoRecurso.MUEBLE;
        //Arrange
        when(recursoDAO.findByTipoRecurso(any(TipoRecurso.class))).thenReturn(Optional.empty());
        //Act
        assertThatThrownBy(() -> recursoService.findByTipoRecurso(tipoRecurso)).isInstanceOf(RuntimeException.class);
        //Assert
        verify(recursoDAO, times(1)).findByTipoRecurso(tipoRecurso);
    }

    @Test
    @DisplayName("GET all - obtiene una lista (no vacia)")
    void getAll_obtieneUnaLista() {
        //Arrange
        when(recursoDAO.findAll()).thenReturn(List.of(recursoDtoValido));

        //Act
        List<RecursoDto> result = recursoService.findAll();

        //Assets
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(recursoDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("UPDATE - actualiza un recurso")
    void update_actualizaUnRecursos(){
        //Arrange
        RecursoDto existing = new RecursoDto();
        existing.setIdRecurso(idRecursoValido);
        existing.setNombre("Teclado Aula F75");
        existing.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        existing.setUnidades(1);
        existing.setDescripcion("Teclado con colores grises");
        existing.setPrecio(7400F);

        RecursoDto update = new RecursoDto();
        update.setNombre("Teclado Aula F78");
        update.setTipoRecurso(TipoRecurso.TECNOLOGICO);
        update.setUnidades(1);
        update.setDescripcion("Teclado con colores morados");
        update.setPrecio(7500F);

        RecursoDto updated = new RecursoDto();
        updated.setIdRecurso(idRecursoValido);
        updated.setNombre("Teclado Aula F78");
        updated.setTipoRecurso(existing.getTipoRecurso());
        updated.setUnidades(existing.getUnidades());
        updated.setDescripcion(existing.getDescripcion());
        updated.setPrecio(existing.getPrecio());

        when(recursoDAO.findById(idRecursoValido)).thenReturn(Optional.of(existing));
        when(recursoDAO.update(eq(idRecursoValido), any(RecursoDto.class))).thenAnswer(invocation -> {
            RecursoDto passed = invocation.getArgument(1);

            return Optional.of(passed);
        });

        //Act
        RecursoDto result = recursoService.update(idRecursoValido, update);

        //Assert - estado
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Teclado Aula F78");
        assertThat(result.getTipoRecurso()).isEqualTo(existing.getTipoRecurso());

        //Assert - comportamiento
        ArgumentCaptor<RecursoDto> captor = ArgumentCaptor.forClass(RecursoDto.class);
        verify(recursoDAO, times(1)).update(eq(idRecursoValido), captor.capture());
    }

    @Test
    @DisplayName("UPDATE - ArrojaRuntimeExceptionSiNoExiste")
    void update_arrojaRuntimeExceptionSiNoExiste(){
        //Arrange
        when(recursoDAO.findById(999L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> recursoService.update(999L, recursoDtoValido))
                .isInstanceOf(RuntimeException.class);

        verify(recursoDAO, times(1)).findById(999L);
        verify(recursoDAO, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE - elimina un recurso")
    void delete_eliminaUnRecursos() {
        // Arrange
        when(recursoDAO.findById(idRecursoValido)).thenReturn(Optional.of(recursoDtoValido));
        when(recursoReservadoService.findAll()).thenReturn(Collections.emptyList());
        when(recursoDAO.delete(idRecursoValido)).thenReturn(true);

        // Act & Assert
        assertThatCode(() -> recursoService.delete(idRecursoValido))
                .doesNotThrowAnyException();

        // Verificaciones
        verify(recursoDAO, times(1)).findById(idRecursoValido);
        verify(recursoReservadoService, times(1)).findAll();
        verify(recursoDAO, times(1)).delete(idRecursoValido);
    }

    @Test
    @DisplayName("DELETE - lanza excepción cuando el recurso está reservado")
    void delete_lanzaExcepcionCuandoTieneReservas() {
        // Arrange
        when(recursoDAO.findById(idRecursoValido)).thenReturn(Optional.of(recursoDtoValido));

        RecursoReservadoDto reserva = new RecursoReservadoDto();
        reserva.setIdReserva(idRecursoValido); // Simula que el recurso está reservado
        when(recursoReservadoService.findAll()).thenReturn(List.of(reserva));

        // Act & Assert
        assertThatThrownBy(() -> recursoService.delete(idRecursoValido))
                .isInstanceOf(IllegalStateException.class);

        // Verificar que NO se llamó a delete()
        verify(recursoDAO, never()).delete(anyLong());
        // Verificaciones adicionales
        verify(recursoDAO, times(1)).findById(idRecursoValido);
        verify(recursoReservadoService, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE - lanza excepcion si no existe el recurso")
    void delete_lanzaExcepcionSiNoExiste() {
        //Arrange
        when(recursoDAO.findById(999L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> recursoService.delete(999L))
                .isInstanceOf(RuntimeException.class);

        verify(recursoDAO, times(1)).findById(999L);
        verify(recursoDAO, never()).delete(anyLong());
    }
}
