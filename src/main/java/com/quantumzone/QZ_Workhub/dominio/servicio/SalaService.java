package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SalaService {

    private final SalaRepository salaRepository;

    @Autowired
    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar una sala
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
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
