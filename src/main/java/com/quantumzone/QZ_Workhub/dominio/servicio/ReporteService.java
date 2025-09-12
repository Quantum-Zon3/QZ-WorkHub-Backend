package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id);
    }

    // Listar todos los reportes
    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    // Eliminar un reporte por id
    public void deleteById(Long id) {
        reporteRepository.deleteById(id);
    }

    // Actualizar un reporte
    public Reporte update(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    // Buscar reportes por filtros
    public Optional<List<Reporte>> findByFecha(LocalDateTime time) {
        return reporteRepository.findByFecha(time);
    }
}
