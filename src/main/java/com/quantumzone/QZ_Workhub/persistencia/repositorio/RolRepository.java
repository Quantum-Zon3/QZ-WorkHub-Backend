package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import com.quantumzone.QZ_Workhub.persistencia.entidad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

}
