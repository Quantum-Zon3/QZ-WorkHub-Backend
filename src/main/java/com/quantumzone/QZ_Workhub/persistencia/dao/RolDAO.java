package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.RolDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Rol;
import com.quantumzone.QZ_Workhub.persistencia.mapper.RolMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RolDAO {
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public RolDto save(RolDto rolDto) {
        Rol rol = rolMapper.toRol(rolDto);
        rolRepository.save(rol);
        return rolMapper.toRolDto(rol);
    }

    public Optional<RolDto> findById(Long id) {
        return rolRepository.findById(id).map(rolMapper::toRolDto);
    }

    public List<RolDto> findAll() {
        List<Rol> roles = rolRepository.findAll();
        return rolMapper.toRolDtos(roles);
    }

    public Optional<RolDto> update (Long id, RolDto rolDto) {
        return rolRepository.findById(id).map(rol -> {
            rolMapper.updateRol(rolDto, rol);
            Rol rolActualizado = rolRepository.save(rol);
            return rolMapper.toRolDto(rolActualizado);
        });
    }

    public boolean delete(Long id) {
        if(rolRepository.existsById(id)){
            rolRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
