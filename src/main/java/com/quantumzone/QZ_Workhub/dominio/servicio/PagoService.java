package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    @Autowired
    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar un pago
    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    // Encontrar un pago por id
    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    // Listar todos los pagos
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    // Eliminar un pago por id
    public void deleteById(Long id) {
         pagoRepository.deleteById(id);
    }

    // Actualizar un pago
    public Pago update(Pago pago) {
        return pagoRepository.save(pago);
    }

    // Buscar pagos por filtros
    public Optional<List<Pago>> findByReserva(Reserva reserva) {
        return pagoRepository.findByReserva(reserva);
    }
    public Optional<List<Pago>> findByReservaUsuarioCedula(Long cedula) {
        return pagoRepository.findByReservaUsuarioCedula(cedula);
    }
}