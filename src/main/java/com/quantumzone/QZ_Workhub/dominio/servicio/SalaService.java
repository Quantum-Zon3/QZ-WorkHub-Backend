package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.SalaDAO;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;

import com.quantumzone.QZ_Workhub.persistencia.repositorio.SalaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio de salas
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @RequiredArgsConstructor - Lombok genera constructor con dependencias final
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de salaDAo (abstracción)
 * - Single Responsibility: Solo lógica de negocio de salas
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class SalaService {


    private final SalaDAO  salaDAO;

    @Autowired
    public SalaService(SalaDAO  salaDAO) {
        this.salaDAO = salaDAO;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar una sala
    public SalaDto save(SalaDto salaDto) {

        log.info("Creando nueva sala con ID: {}", salaDto.getIdSala());
        if (salaDAO.findById(salaDto.getIdSala()).isPresent()) {
            log.warn ("intento crear una sala con un ID duplicado: {}", salaDto.getIdSala());
            throw new IllegalArgumentException("La sala ya existe:" + salaDto.getIdSala());
        }
        validarSala(salaDto);

        SalaDto salaCreada = salaDAO.save(salaDto);
        log.info("Sala con ID creada: {}", salaCreada.getIdSala());
        return salaCreada;
    }
    private void validarSala(SalaDto salaDto) {
        //validar ID
        if(salaDto.getIdSala() == null || salaDto.getIdSala() <= 0) {
            throw new IllegalArgumentException("La sala es obligatoria y no puede ser negativa");
        }

        //validar nombre
        if(salaDto.getNombre() == null || salaDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if(salaDto.getNombre().length() < 45) {
            throw new IllegalArgumentException("El nombre de la sala no puede exceder los 45 caracteres");
        }

        //validar capacidad
        if(salaDto.getCapacidad() <= 0 || salaDto.getCapacidad() == null) {
            throw new IllegalArgumentException("La sala debe tener alguna capacidad fija mayor a 0");
        }


    }

    // Encontrar una sala por id
    public Optional<Sala> findById(Long id) {
        return salaRepository.findById(id);
    }

    // Listar todas las salas
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    // Eliminar una sala por id
    public void deleteById(Long id) {
        salaRepository.deleteById(id);
    }

    // Actualizar una sala
    public Sala update(Sala sala) {
        return salaRepository.save(sala);
    }

    // Buscar salas por filtros
    public Optional<List<Sala>> findByNombre(String nombre) {
        return salaRepository.findByNombre(nombre);
    }
}
