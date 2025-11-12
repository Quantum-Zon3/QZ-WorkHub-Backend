package com.quantumzone.QZ_Workhub.persistencia.repositorio;

import java.util.List;
import java.util.Optional;
import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
import com.quantumzone.QZ_Workhub.persistencia.entidad.Usuario;
//imports de conexion base de datos
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
