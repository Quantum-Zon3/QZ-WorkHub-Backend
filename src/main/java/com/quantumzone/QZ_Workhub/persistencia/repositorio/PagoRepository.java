package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class PagoRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar pago
    public Pago save(Pago Pago) {
        entityManager.persist(Pago);
        return Pago;
    }
    //Buscar todos los pagos
    @Transactional
    public List<Pago> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM pago ", Pago.class);
        return query.getResultList();
    }

    //Buscar pago por id
    @Transactional
    public Optional<Pago> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM pago WHERE id = :id", Pago.class);
        query.setParameter("id", id);
        try {
            Pago pago = (Pago) query.getSingleResult();
            return Optional.of(pago);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar pago por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM pago WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar pago
    public Optional<Pago>update(Integer id ,Pago pago) {
        Query query = entityManager.createNativeQuery("UPDATE pago SET nombre = :nombre WHERE id = :id");
        query.setParameter("id", pago.getNombre());
        query.setParameter("id", pago.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar pago por filtros
    public Optional<List<Pago>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM pago WHERE cedula LIKE :cedula", Pago.class);
        query.setParameter("cedula", cedula );
        try {
            List<Pago> pago = query.getResultList();
            return Optional.of(pago);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
