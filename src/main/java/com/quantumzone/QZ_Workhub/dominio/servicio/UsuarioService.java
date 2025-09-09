package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si quieres
    }

    // Guardar un usuario
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Encontrar un usuario por id
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Listar todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Eliminar un usuario por id
    public boolean deleteById(Integer id) {
        return usuarioRepository.deleteById(id);
    }

    // Actualizar un usuario
    public Optional<Usuario> update(Integer id, Usuario usuario) {
        return usuarioRepository.update(id, usuario);
    }

    // Buscar usuario por filtros
    public Optional<List<Usuario>> findByFilters(String cedula) {
        return usuarioRepository.findByFilters(cedula);
    }
}
