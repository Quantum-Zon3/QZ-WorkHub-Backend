package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.Reporte;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    @Autowired
    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un reporte
    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    // Encontrar un reporte por id
    public Optional<Reporte> findById(Integer id) {
        return reporteRepository.findById(id);
    }

    // Listar todos los reportes
    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    // Eliminar un reporte por id
    public boolean deleteById(Integer id) {
        return reporteRepository.deleteById(id);
    }

    // Actualizar un reporte
    public Optional<Reporte> update(Integer id, Reporte reporte) {
        return reporteRepository.update(id, reporte);
    }

    // Buscar reportes por filtros (ejemplo: tipo, estado o fecha)
    public Optional<List<Reporte>> findByFilters(String filtro) {
        return reporteRepository.findByFilters(filtro);
    }
}
