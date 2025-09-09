package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;

@Repository
public class NotificacionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //Guardar notificacion
    public Notificacion save(Notificacion noti) {
        entityManager.persist(noti);
        return noti;
    }
    //Buscar todos los notificaciones
    @Transactional
    public List<Notificacion> findAll() {
        Query query = entityManager.createNativeQuery("SELECT * FROM notificacion ", Notificacion.class);
        return query.getResultList();
    }

    //Buscar notificacion por id
    @Transactional
    public Optional<Notificacion> findById(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM notificacion WHERE id = :id", Notificacion.class);
        query.setParameter("id", id);
        try {
            Notificacion noti = (Notificacion) query.getSingleResult();
            return Optional.of(noti);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //Eliminar notificacion por id
    @Transactional
    public boolean deleteById(Integer id) {
        Query query = entityManager.createNativeQuery("DELETE FROM notificacion WHERE id = :id");
        query.setParameter("id", id);
        int delete = query.executeUpdate();
        return delete > 0;
    }

    //Actualizar notificacion
    public Optional<Notificacion>update(Integer id ,Notificacion noti) {
        Query query = entityManager.createNativeQuery("UPDATE notificacion SET nombre = :nombre WHERE id = :id");
        query.setParameter("id", noti.getNombre());
        query.setParameter("id", noti.getId());
        int update = query.executeUpdate();
        if (update > 0) {
            return findById(id);
        } else {
            return Optional.empty();
        }
    }

    //Buscar notificacion por filtros
    public Optional<List<Notificacion>> findByFilters(String cedula) {
        Query query = entityManager.createNativeQuery("SELECT * FROM notificacion WHERE cedula LIKE :cedula", Notificacion.class);
        query.setParameter("cedula", cedula );
        try {
            List<Notificacion> noti = query.getResultList();
            return Optional.of(noti);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
