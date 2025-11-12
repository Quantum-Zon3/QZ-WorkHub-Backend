package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
import com.quantumzone.QZ_Workhub.persistencia.mapper.UsuarioMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioDAO {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDto save(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.toUsuario(usuarioDto);
        usuarioRepository.save(usuario);
        return usuarioMapper.toUsuarioDto(usuario);
    }

    public Optional<UsuarioDto> findById(Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toUsuarioDto);
    }

    public List<UsuarioDto> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toUsuarioDtos(usuarios);
    }

    public Optional<UsuarioDto> update(Long id, UsuarioDto usuarioDto) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioMapper.updateUsuario(usuarioDto, usuario);
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.toUsuarioDto(usuarioActualizado);
        });
    }

    public boolean delete(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
