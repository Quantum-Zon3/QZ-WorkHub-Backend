package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;
import com.quantumzone.QZ_Workhub.persistencia.mapper.ReporteMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReporteDAO {
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    public ReporteDto save(ReporteDto reporteDto){
        Reporte reporte = reporteMapper.toReporte(reporteDto);
        reporteRepository.save(reporte);
        return reporteMapper.toReporteDto(reporte);
    }

    public Optional<ReporteDto> findById(Long id){
        return reporteRepository.findById(id).map(reporteMapper::toReporteDto);
    }

    public List<ReporteDto> findAll(){
        List<Reporte> reportes = reporteRepository.findAll();
        return reporteMapper.toReporteDtos(reportes);
    }

    public Optional<List<ReporteDto>> findByFecha(LocalDateTime fecha){
        return reporteRepository.findByFecha(fecha).map(reporteMapper::toReporteDtos);
    }

    public Optional<ReporteDto> update(Long id, ReporteDto reporteDto){
        return reporteRepository.findById(id).map(reporte -> {
            reporteMapper.updateReporte(reporteDto, reporte);
            Reporte reporteActualizado = reporteRepository.save(reporte);
            return reporteMapper.toReporteDto(reporteActualizado);
        });
    }

    public boolean delete(Long id) {
        if (reporteRepository.existsById(id)) {
            reporteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
