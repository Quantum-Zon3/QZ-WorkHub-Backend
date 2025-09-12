package com.quantumzone.QZ_Workhub.dominio.servicio;

import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RecursoReservadoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class RecursoReservadoService {

    private final RecursoReservadoRepository recursoReservadoRepository;

    @Autowired
    public RecursoReservadoService(RecursoReservadoRepository recursoReservadoRepository) {
        this.recursoReservadoRepository = recursoReservadoRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un recurso reservado
    public RecursoReservado save(RecursoReservado recursoReservado) {
        return recursoReservadoRepository.save(recursoReservado);
    }

    // Encontrar un recurso reservado por id
    public Optional<RecursoReservado> findById(Long id) {
        return recursoReservadoRepository.findById(id);
    }

    // Listar todos los recursos reservados
    public List<RecursoReservado> findAll() {
        return recursoReservadoRepository.findAll();
    }

    // Eliminar un recurso reservado por id
    public void deleteById(Long id) {recursoReservadoRepository.deleteById(id);
    }

    // Actualizar un recurso reservado
    public RecursoReservado update(RecursoReservado recursoReservado) {
        return recursoReservadoRepository.save(recursoReservado);
    }
    /*
    // Buscar recursos reservados por filtros (ejemplo: idUsuario o fecha)
    public Optional<List<RecursoReservado>> findByFilters(String filtro) {
        return recursoReservadoRepository.findByFilters(filtro);
    }
     */
}

