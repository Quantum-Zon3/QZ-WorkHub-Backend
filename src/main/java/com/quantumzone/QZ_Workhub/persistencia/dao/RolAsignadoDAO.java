package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.RolAsignadoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RolAsignado;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RolAsignadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RolAsignadoDAO {
    private final RolAsignadoRepository rolAsignadoRepository;
    private final RolAsignadoMapper rolAsignadoMapper;

    public RolAsignadoDto save(RolAsignadoDto rolAsignadoDto) {
        RolAsignado rolAsignado = rolAsignadoMapper.toRolAsignado(rolAsignadoDto);
        rolAsignadoRepository.save(rolAsignado);
        return rolAsignadoMapper.toRolAsignadoDto(rolAsignado);
    }

    public Optional<RolAsignadoDto> findById(Long id) {
        return rolAsignadoRepository.findById(id).map(rolAsignadoMapper::toRolAsignadoDto);
    }

    public List<RolAsignadoDto> findAll() {
        List<RolAsignado> rolesAsignados = rolAsignadoRepository.findAll();
        return rolAsignadoMapper.toRolAsignadoDtos(rolesAsignados);
    }

    public Optional<RolAsignadoDto> update (Long id, RolAsignadoDto rolAsignadoDto) {
        return rolAsignadoRepository.findById(id).map(rolAsignado -> {
           rolAsignadoMapper.updateRolAsignado(rolAsignadoDto, rolAsignado);
           RolAsignado rolAsignadoActualizado = rolAsignadoRepository.save(rolAsignado);
           return rolAsignadoMapper.toRolAsignadoDto(rolAsignadoActualizado);
        });
    }

    public boolean delete(Long id) {
        if (rolAsignadoRepository.existsById(id)) {
            rolAsignadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
