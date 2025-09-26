package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;
import com.quantumzone.QZ_Workhub.persistencia.mapper.SalaMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalaDAO {
    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    public SalaDto save(SalaDto salaDto) {
        Sala sala = salaMapper.toSala(salaDto);
        sala = salaRepository.save(sala);
        return salaMapper.toSalaDto(sala);
    }

    public Optional<SalaDto> findById(Long id) {
        return salaRepository.findById(id).map(salaMapper::toSalaDto);
    }

    public List<SalaDto> findAll(){
        List<Sala> salas = salaRepository.findAll();
        return salaMapper.toSalaDtos(salas);
    }

    /*
    * Método para obtener una sala o salas por medio del nombre
     */

    public Optional<List<SalaDto>> findSalaByName(String name) {
        return salaRepository.findByNombre(name).map(salaMapper::toSalaDtos);
    }

    public Optional<SalaDto> update(Long id, SalaDto salaDto) {
        return salaRepository.findById(id).map(sala -> {
            salaMapper.updateSala(salaDto, sala);
            Sala salaActualizada = salaRepository.save(sala);
            return salaMapper.toSalaDto(salaActualizada);
        });
    }

    public boolean delete(Long id) {
        if (salaRepository.existsById(id)) {
            salaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
