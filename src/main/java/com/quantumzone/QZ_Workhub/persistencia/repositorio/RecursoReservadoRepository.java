package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.RecursoReservado;

@Repository
public class RecursoReservadoRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar recursoReservado
    public RecursoReservado save(RecursoReservado recursoR) {
        entityManager.persist(recursoR);
        return recursoR;
    }
    //Buscar todos los recursoReservado
    @Transactional
    public List<RecursoReservado> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso_reservado ", RecursoReservado.class);
        return query.getResultList();
    }

    //Buscar recursoReservado por id
    @Transactional
    public Optional<RecursoReservado> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso_reservado WHERE id = :id", RecursoReservado.class);
        query.setParameter("id", id);
        try {
            RecursoReservado recursoR = (RecursoReservado) query.getSingleResult();
            return Optional.of(recursoR);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar recursoReservado por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM recurso_reservado WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar recursoReservado
    public Optional<RecursoReservado>update(Integer id ,RecursoReservado recursoR) {
        Query query = entityManager.createNativeQuery("UPDATE recurso_reservado SET nombre = :nombre WHERE id = :id");
        query.setParameter("id", recursoR.getNombre());
        query.setParameter("id", recursoR.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar recursoReservado por filtros
    public Optional<List<RecursoReservado>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM recurso_reservado WHERE cedula LIKE :cedula", RecursoReservado.class);
        query.setParameter("cedula", cedula );
        try {
            List<RecursoReservado> recursoR = query.getResultList();
            return Optional.of(recursoR);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
