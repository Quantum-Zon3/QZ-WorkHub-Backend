package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;

@Repository
public class UsuarioRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar usuario
    public Usuario save(Usuario usuario){
        entityManager.persist(usuario);
        return usuario;
    }
    //Buscar todos los usuarios
    @Transactional
    public List<Usuario> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuario ", Usuario.class);
        return query.getResultList();
    }

    //Buscar usuario por id
    @Transactional
    public Optional<Usuario> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuario WHERE id = :id", Usuario.class);
        query.setParameter("id", id);
        try {
            Usuario usuario = (Usuario) query.getSingleResult();
            return Optional.of(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar usuario por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM usuario WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar usuario
    public Optional<Usuario>update(Integer id,Usuario usuario) {
        Query query = entityManager.createNativeQuery("UPDATE clientes SET nombre = :nombre, edad = :edad, direccion = :direccion, imagen = :imagen, cedula = :cedula, telefono = :telefono, fechaRegistro = :fechaRegistro, email = :email WHERE id = :id");
        query.setParameter("nombre", usuario.getNombre());
        query.setParameter("edad", usuario.getEdad());
        query.setParameter("direccion", usuario.getDireccion());
        query.setParameter("imagen", usuario.getImagen());
        query.setParameter("cedula", usuario.getCedula());
        query.setParameter("telefono", usuario.getTelefono());
        query.setParameter("fechaRegistro", usuario.getFechaRegistro());
        query.setParameter("email", usuario.getEmail());
        query.setParameter("id", usuario.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar usuario por filtros
    public Optional<List<Usuario>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM usuario WHERE cedula LIKE :cedula", Usuario.class);
        query.setParameter("cedula", cedula );
        try {
            List<Usuario> usuario = query.getResultList();
            return Optional.of(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
