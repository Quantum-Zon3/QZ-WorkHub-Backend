package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RecursoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;

    @Autowired
    public RecursoService(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un recurso
    public Recurso save(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    // Encontrar un recurso por id
    public Optional<Recurso> findById(Long id) {
        return recursoRepository.findById(id);
    }

    // Listar todos los recursos
    public List<Recurso> findAll() {
        return recursoRepository.findAll();
    }

    // Eliminar un recurso por id
    public void deleteById(Long id) {recursoRepository.deleteById(id);
    }

    // Actualizar un recurso
    public Recurso update(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    // Buscar recursos por filtros
    public Optional<List<Recurso>> findByTipo(TipoRecurso tipo) {
        return recursoRepository.findRecursosByTipo(tipo);
    }
}

