package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;

@Repository
public class ReservaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar reserva
    public Reserva save(Reserva reserva) {
        entityManager.persist(reserva);
        return reserva;
    }

    //Buscar todas las reservas
    @Transactional
    public List<Reserva> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM reserva ", Reserva.class);
        return query.getResultList();
    }

    //Busca por id un sala
    @Transactional
    public Optional<Reserva> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM reserva WHERE id = :id", Reserva.class);
        query.setParameter("id", id);
        try {
            Reserva reserva = (Reserva) query.getSingleResult();
            return Optional.of(reserva);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Elimina por id una reserva
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM reserva WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar por id una reserva
    public Optional<Reserva> update(Integer id, Reserva reserva) {
        Query query = entityManager.createNativeQuery("UPDATE reserva SET nombre = :nombre");
        query.setParameter("nombre", reserva.getNombre());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar reservas por filtros
    public Optional<List<Reserva>> findByFilters(String nombre) {
        Query query = entityManager.createNativeQuery("SELECT * FROM reserva WHERE nombre LIKE :nombre", Reserva.class);
        query.setParameter("nombre", nombre);
        try {
            List<Reserva> reserva = query.getResultList();
            return Optional.of(reserva);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
