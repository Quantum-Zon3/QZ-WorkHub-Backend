package com.quantumzone.QZ_Workhub.persistencia.dao;


import com.quantumzone.QZ_Workhub.dominio.dto.RecursoReservadoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;
import com.quantumzone.QZ_Workhub.persistencia.mapper.RecursoReservadoMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RecursoReservadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecursoReservadoDAO {
    private final RecursoReservadoRepository recursoReservadoRepository;
    private final RecursoReservadoMapper recursoReservadoMapper;

    public RecursoReservadoDto save(RecursoReservadoDto recursoRDto) {
        RecursoReservado recursoR = recursoReservadoMapper.toRecursoReservado(recursoRDto);
        recursoR = recursoReservadoRepository.save(recursoR);
        return recursoReservadoMapper.toRecursoReservadoDto(recursoR);
    }

    public Optional<RecursoReservadoDto> findById(Long id) {
        return recursoReservadoRepository.findById(id).map(recursoReservadoMapper::toRecursoReservadoDto);
    }

    public List<RecursoReservadoDto> findAll() {
        List<RecursoReservado> recursosReservados = recursoReservadoRepository.findAll();
        return recursoReservadoMapper.toRecursoReservadoDtos(recursosReservados);
    }

    public Optional<RecursoReservadoDto> update(Long id, RecursoReservadoDto recursoRDto) {
        return recursoReservadoRepository.findById(id).map(recursoReservado -> {
            recursoReservadoMapper.updateRecursoReservado(recursoRDto, recursoReservado);
            RecursoReservado recursoReActualizado = recursoReservadoRepository.save(recursoReservado);
            return recursoReservadoMapper.toRecursoReservadoDto(recursoReActualizado);
        });
    }

    public boolean delete(Long id) {
        if (recursoReservadoRepository.existsById(id)) {
            recursoReservadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
