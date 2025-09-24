package com.quantumzone.QZ_Workhub.persistencia.dao;


import com.quantumzone.QZ_Workhub.dominio.dto.NotificacionDto;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Notificacion;
import com.quantumzone.QZ_Workhub.persistencia.mapper.NotificacionMapper;
import com.quantumzone.QZ_Workhub.persistencia.repositorio.NotificacionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificacionDAO {
    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    public NotificacionDto save(NotificacionDto notificacionDto) {
        Notificacion noti = notificacionMapper.toNotificacion(notificacionDto);
        notificacionRepository.save(noti);
        return notificacionMapper.toNotificacionDto(noti);
    }

    public Optional<NotificacionDto> findById(Long id){
        return notificacionRepository.findById(id).map(notificacionMapper::toNotificacionDto);
    }

    public List<NotificacionDto> findAll(){
        List<Notificacion> notificaciones = notificacionRepository.findAll();
        return notificacionMapper.toNotificacionDtos(notificaciones);
    }

    public Optional<NotificacionDto> update(Long id, NotificacionDto notificacionDto) {
        return notificacionRepository.findById(id).map(notificacion -> {
            notificacionMapper.updateNotificacion(notificacionDto, notificacion);
            Notificacion notiActualizada = notificacionRepository.save(notificacion);
            return notificacionMapper.toNotificacionDto(notificacion);
        });
    }

    public boolean delete(Long id){
        if(notificacionRepository.existsById(id)){
            notificacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

