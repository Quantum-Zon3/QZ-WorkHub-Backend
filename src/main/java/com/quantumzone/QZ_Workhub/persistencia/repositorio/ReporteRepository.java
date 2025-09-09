package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reporte;

@Repository
public class ReporteRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar reporte
    public Reporte save(Reporte reporte) {
        entityManager.persist(reporte);
        return reporte;
    }
    //Buscar todos los reportes
    @Transactional
    public List<Reporte> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM reporte ", Reporte.class);
        return query.getResultList();
    }

    //Buscar reporte por id
    @Transactional
    public Optional<Reporte> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM reporte WHERE id = :id", Reporte.class);
        query.setParameter("id", id);
        try {
            Reporte reporte = (Reporte) query.getSingleResult();
            return Optional.of(reporte);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar reporte por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM reporte WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar reporte
    public Optional<Reporte>update(Integer id,Reporte reporte) {
        Query query = entityManager.createNativeQuery("UPDATE reporte SET nombre = :nombre WHERE id = :id");
        query.setParameter("id", reporte.getNombre());
        query.setParameter("id", reporte.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar reporte por filtros
    public Optional<List<Reporte>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM reporte WHERE cedula LIKE :cedula", Reporte.class);
        query.setParameter("cedula", cedula );
        try {
            List<Reporte> reporte = query.getResultList();
            return Optional.of(reporte);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
