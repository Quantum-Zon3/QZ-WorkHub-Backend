package com.quantumzone.QZ_Workhub.persistencia.dao;

import com.quantumzone.QZ_Workhub.dominio.dto.PagoDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Pago;
import com.quantumzone.QZ_Workhub.persistencia.mapper.NotificacionMapper;
import com.quantumzone.QZ_Workhub.persistencia.mapper.PagoMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PagoDAO {
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    public PagoDto save(PagoDto pagoDto) {
        Pago pago = pagoMapper.toPago(pagoDto);
        pagoRepository.save(pago);
        return pagoMapper.toPagoDto(pago);
    }

    public Optional<PagoDto> findById(Long id) {
        return pagoRepository.findById(id).map(pagoMapper::toPagoDto);
    }

    public List<PagoDto> findAll() {
        List<Pago> pagos = pagoRepository.findAll();
        return pagoMapper.toPagoDtos(pagos);
    }

    public Optional<PagoDto> update(Long id, PagoDto pagoDto){
            return pagoRepository.findById(id).map(pago -> {
                pagoMapper.updatePago(pagoDto, pago);
                Pago pagoActualizado = pagoRepository.save(pago);
                return pagoMapper.toPagoDto(pagoActualizado);
            });
    }

    public boolean delete(Long id) {
        if(pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
