package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Recurso;

@Repository
public class RecursoRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar recurso
    public Recurso save(Recurso recurso) {
        entityManager.persist(recurso);
        return recurso;
    }
    //Buscar todos los recurso
    @Transactional
    public List<Recurso> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso", Recurso.class);
        return query.getResultList();
    }

    //Buscar recurso por id
    @Transactional
    public Optional<Recurso> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso WHERE id = :id", Recurso.class);
        query.setParameter("id", id);
        try {
            Recurso recurso = (Recurso) query.getSingleResult();
            return Optional.of(recurso);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar recurso por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM recurso WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar recurso
    public Optional<Recurso>update(Integer id ,Recurso recurso) {
        Query query = entityManager.createNativeQuery("UPDATE recurso SET nombre = :nombre WHERE id = :id");
        query.setParameter("id", recurso.getNombre());
        query.setParameter("id", recurso.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar recurso por filtros
    public Optional<List<Recurso>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso WHERE cedula LIKE :cedula", Recurso.class);
        query.setParameter("cedula", cedula );
        try {
            List<Recurso> recurso = query.getResultList();
            return Optional.of(recurso);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
