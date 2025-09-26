package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Reserva;
import com.quantumzone.QZ_Workhub.persistencia.mapper.ReservaMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReporteRepository;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
