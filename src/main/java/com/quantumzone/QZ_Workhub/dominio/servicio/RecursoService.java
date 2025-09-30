package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.persistencia.dao.RecursoDAO;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RecursoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de recurso
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de SellerDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de vendedores
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class RecursoService {

    private final RecursoDAO recursoDao;

    @Autowired
    public RecursoService(RecursoDAO recursoDao) {
        this.recursoDao = recursoDao;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un recurso
    public RecursoDto save(RecursoDto recursoDto) {
        log.info("Creando nuevo recurso con id: {}", recursoDto.getIdRecurso());

        //Validaciones de negocio
        validarRecurso(recursoDto);

        //Creacion del recurso
        RecursoDto recursoCreado = recursoDao.save(recursoDto);
        log.info("Recurso creado: {}", recursoCreado.getIdRecurso());
        return recursoCreado;
    }

    // Encontrar un recurso por id
    public RecursoDto findById(Long id) {
        log.info("Buscando recurso con id: {}", id);

        return recursoDao.findById(id).orElseThrow(() -> {
            log.warn("recurso no encontrada con ID: {}", id);
            return new RuntimeException("recurso no encontrada con ID: " + id);
        });
    }

    // Listar todos los recursos
    @Transactional(readOnly = true)
    public List<RecursoDto> findAll() {
        log.debug("Obteniendo todos los recursos: {}", recursoDao.findAll().size());
        return recursoDao.findAll();
    }

    // Eliminar un recurso por id
    public void delete(Long id) {
        log.info("Eliminando recurso ID: {}", id);

        // Verificar que el recurso existe
        findById(id);

        // Eliminar recurso
        boolean deleted = recursoDao.delete(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar recurso con ID: " + id);
        }

        log.info("Recurso eliminado exitosamente ID: {}", id);
    }

    public RecursoDto update(Long id, RecursoDto recursoDto) {
        log.info("Actualizando recurso ID: {}", id);

        // Verificar que el recurso existe
        findById(id);

        // Validar datos de actualización
        validarRecurso(recursoDto);

        // Actualizar
        RecursoDto recursoActualizado = recursoDao.update(id, recursoDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar recurso"));

        log.info("recurso actualizado exitosamente ID: {}", id);
        return recursoActualizado;
    }

    // Buscar recursos por tipo
    public List<RecursoDto> findByTipoRecurso(TipoRecurso tipo) {
        log.info("Buscando recurso con tipo: {}", tipo);

        return recursoDao.findByTipoRecurso(tipo).orElseThrow(() -> {
            log.warn("recurso no encontrada con tipo: {}", tipo);
            return new RuntimeException("recurso no encontrada con tipo: " + tipo);
        });

    }

    private void validarRecurso(RecursoDto recursoDto) {

        // Validar nombre
        if (recursoDto.getNombre() == null || recursoDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del recurso es obligatorio");
        }
        if (recursoDto.getNombre().length() > 45) {
            throw new IllegalArgumentException("El nombre no puede exceder 45 caracteres");
        }

        // Validar tipo de recurso
        if (recursoDto.getTipoRecurso() == null) {
            throw new IllegalArgumentException("El tipo de recurso es obligatorio");
        }

        // Validar unidades del recurso
        if (recursoDto.getUnidades() == null || recursoDto.getUnidades() < 0) {
            throw new IllegalArgumentException("Las unidades del recursos son obligatorio y no pueden ser negativas");
        }

        // Validar descripcion del recurso
        if (recursoDto.getDescripcion().trim().isEmpty() || recursoDto.getDescripcion().length() > 45) {
            throw new IllegalArgumentException("La descripción no puede exceder 200 caracteres");
        }

        //Validar precio
        if (recursoDto.getPrecio() == null || recursoDto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio de el recurso no es válido");
        }
    }
}

