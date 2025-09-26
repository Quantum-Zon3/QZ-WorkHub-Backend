package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.RecursoDto;
import com.quantumzone.QZ_Workhub.dominio.enums.TipoRecurso;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;
import com.quantumzone.QZ_Workhub.persistencia.mapper.NotificacionMapper;
import com.quantumzone.QZ_Workhub.persistencia.mapper.RecursoMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecursoDAO {
    private final RecursoRepository recursoRepository;
    private final RecursoMapper recursoMapper;
    private final NotificacionMapper notificacionMapper;

    public RecursoDto save(RecursoDto recursoDto) {
        Recurso recurso = recursoMapper.toRecurso(recursoDto);
        recursoRepository.save(recurso);
        return recursoMapper.toRecursoDto(recurso);
    }

    public Optional<RecursoDto> findById(Long id) {
        return recursoRepository.findById(id).map(recursoMapper::toRecursoDto);
    }

    /*
    * metodo para buscar todos los recursos por tipo de recurso
    * MUEBLE ó TECNOLÓGICO
    */

    public Optional<List<RecursoDto>> findByTipoRecurso(TipoRecurso tipoRecurso) {
        return recursoRepository.findRecursosByTipo(tipoRecurso).map(recursoMapper::toRecursoDtos);
    }

    public List<RecursoDto> findAll() {
        List<Recurso> recursos = recursoRepository.findAll();
        return recursoMapper.toRecursoDtos(recursos);
    }

    public Optional<RecursoDto> update(Long id, RecursoDto recursoDto) {
        return recursoRepository.findById(id).map(recurso -> {
            recursoMapper.updateRecurso(recursoDto, recurso);
            Recurso recursoActualizado = recursoRepository.save(recurso);
            return recursoMapper.toRecursoDto(recursoActualizado);
        });
    }

    public boolean delete(Long id){
        if(recursoRepository.existsById(id)){
            recursoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
