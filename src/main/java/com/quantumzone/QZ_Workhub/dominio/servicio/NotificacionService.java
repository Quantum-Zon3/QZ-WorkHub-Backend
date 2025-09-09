package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.NotificacionRepository;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí podrías cargar datos iniciales de prueba si deseas
    }

    // Guardar una notificación
    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    // Encontrar una notificación por id
    public Optional<Notificacion> findById(Integer id) {
        return notificacionRepository.findById(id);
    }

    // Listar todas las notificaciones
    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    // Eliminar una notificación por id
    public boolean deleteById(Integer id) {
        return notificacionRepository.deleteById(id);
    }

    // Actualizar una notificación
    public Optional<Notificacion> update(Integer id, Notificacion notificacion) {
        return notificacionRepository.update(id, notificacion);
    }

    // Buscar notificaciones por filtros
    public Optional<List<Notificacion>> findByFilters(String tipo) {
        return notificacionRepository.findByFilters(tipo);
    }
}
