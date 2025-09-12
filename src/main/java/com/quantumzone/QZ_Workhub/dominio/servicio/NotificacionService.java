package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
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

    }
    // Guardar una notificación
    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    // Encontrar una notificación por id
    public Optional<Notificacion> findById(Long id) {
        return notificacionRepository.findById(id);
    }

    // Listar todas las notificaciones
    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    // Eliminar una notificación por id
    public void deleteById(Long id) { notificacionRepository.deleteById(id);
    }

    // Actualizar una notificación
    public Notificacion update(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    // Buscar notificaciones por filtros
    public Optional<List<Notificacion>> findByReserva(Reserva reserva) {
        return notificacionRepository.findByReserva(reserva);
    }
    public Optional<List<Notificacion>> findByReserva(Long cedula) {
        return notificacionRepository.findByReservaUsuarioCedula(cedula);
    }

}
