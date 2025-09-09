package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReservaRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar una reserva
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Encontrar una reserva por id
    public Optional<Reserva> findById(Integer id) {
        return reservaRepository.findById(id);
    }

    // Listar todas las reservas
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    // Eliminar una reserva por id
    public boolean deleteById(Integer id) {
        return reservaRepository.deleteById(id);
    }

    // Actualizar una reserva
    public Optional<Reserva> update(Integer id, Reserva reserva) {
        return reservaRepository.update(id, reserva);
    }

    // Buscar reservas por filtros (ejemplo: usuario, fecha o estado)
    public Optional<List<Reserva>> findByFilters(String filtro) {
        return reservaRepository.findByFilters(filtro);
    }
}

