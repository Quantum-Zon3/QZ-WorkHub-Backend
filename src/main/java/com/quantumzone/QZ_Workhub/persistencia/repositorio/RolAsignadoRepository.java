package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import com.quantumzone.QZ_Workhub.persistencia.entidad.RolAsignado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolAsignadoRepository extends JpaRepository<RolAsignado, Long> {

}
