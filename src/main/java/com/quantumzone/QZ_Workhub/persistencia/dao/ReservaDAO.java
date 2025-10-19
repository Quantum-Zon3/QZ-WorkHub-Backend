package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import com.quantumzone.QZ_Workhub.persistencia.mapper.ReservaMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReporteRepository;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservaDAO {
    final private ReservaRepository reservaRepository;
    final private ReservaMapper reservaMapper;

    public ReservaDto save(ReservaDto reservaDto){
        Reserva reserva = reservaMapper.toReserva(reservaDto);
        reservaRepository.save(reserva);
        return reservaMapper.toReservaDto(reserva);
    }

    public Optional<ReservaDto> findById(Long id){
        return reservaRepository.findById(id).map(reservaMapper::toReservaDto);
    }

    public List<ReservaDto> findAll(){
        List<Reserva> reservas = reservaRepository.findAll();
        return reservaMapper.toReservaDtos(reservas);
    }
    
    /*
    * Método para obetener las reservas que tengan una misma cantidad de visitantes
     */

    public Optional<List<ReservaDto>> findByCantidadDeVisitantes(Integer cantidadDeVisitantes){
        return reservaRepository.findByCantidadVisitantes(cantidadDeVisitantes).map(reservaMapper::toReservaDtos);
    }

    public Optional<ReservaDto> update(Long id, ReservaDto reservaDto){
        return reservaRepository.findById(id).map(reserva -> {
            reservaMapper.updateReserva(reservaDto, reserva);
            Reserva reservaActualizada = reservaRepository.save(reserva);
            return reservaMapper.toReservaDto(reservaActualizada);
        });
    }

    public boolean delete(Long id){
        if(reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * ✅ Verifica si existe un conflicto de reserva en la misma sala y franja horaria.
     * @param idSala ID de la sala.
     * @param fechaInicio Fecha/hora de inicio propuesta.
     * @param fechaFin Fecha/hora de fin propuesta.
     * @return true si hay conflicto (la sala ya está reservada en ese horario).
     */
    public boolean existeConflicto(Long idSala, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return reservaRepository.existeConflicto(idSala, fechaInicio, fechaFin);
    }

    /**
     * 🛠️ Versión para actualización: excluye la misma reserva del chequeo
     */
    public boolean existeConflictoExcluyendoActual(Long idReserva, Long idSala, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return reservaRepository.existeConflictoExcluyendoActual(idReserva, idSala, fechaInicio, fechaFin);
    }

}
