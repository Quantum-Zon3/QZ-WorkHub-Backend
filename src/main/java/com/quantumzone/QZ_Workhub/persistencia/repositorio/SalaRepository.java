package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Sala;

@Repository
public class SalaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar sala
    public Sala save(Sala sala) {
        entityManager.persist(sala);
        return sala;
    }

    //Buscar todas las sala
    @Transactional
    public List<Sala> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM sala ", Sala.class);
        return query.getResultList();
    }

    //Busca por id un sala
    @Transactional
    public Optional<Sala> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM sala WHERE id = :id", Sala.class);
        query.setParameter("id", id);
        try {
            Sala sala = (Sala) query.getSingleResult();
            return Optional.of(sala);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Elimina por id una sala
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM sala WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar por id una sala
    public Optional<Sala> update(Integer id, Sala sala) {
        Query query = entityManager.createNativeQuery("UPDATE sala SET nombre = :nombre");
        query.setParameter("nombre", sala.getNombre());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar sala por filtros
    public Optional<List<Sala>> findByFilters(String nombre) {
        Query query = entityManager.createNativeQuery("SELECT * FROM sala WHERE nombre LIKE :nombre", Sala.class);
        query.setParameter("nombre", nombre);
        try {
            List<Sala> sala = query.getResultList();
            return Optional.of(sala);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}