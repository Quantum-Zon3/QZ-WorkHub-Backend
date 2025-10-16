package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.ReporteDto;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.UsuarioDto;
import com.quantumzone.QZ_Workhub.dominio.enums.Rol;
import com.quantumzone.QZ_Workhub.persistencia.dao.UsuarioDAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Implementación del servicio de usuarios
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de usuarioDAo (abstracción)
 * - Single Responsibility: Solo lógica de negocio de usuarios
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final ReservaService reservaService;
    private final ReporteService reporteService;

    @Autowired
    public UsuarioService(UsuarioDAO usuarioDAO, ReservaService reservaService, @Lazy ReporteService reporteService) {
        this.usuarioDAO = usuarioDAO;
        this.reservaService = reservaService;
        this.reporteService = reporteService;
        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si quieres
    }
    /**
     * Crear un nuevo usuario con validaciones de negocio
     */
    public UsuarioDto save(UsuarioDto usuarioDto) {
        log.info("Creando nuevo usuario con cedula: {}", usuarioDto.getCedula());

        // Validación de negocio: Email único
        if (usuarioDAO.findById(usuarioDto.getCedula()).isPresent()) {
            log.warn("Intento de crear usuario con email duplicado: {}", usuarioDto.getCedula());
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioDto.getCedula());
        }

        // Validaciones adicionales de negocio
        validarUsuario(usuarioDto);

        // Crear usuario
        UsuarioDto usaurioCreado = usuarioDAO.save(usuarioDto);
        log.info("Usuario creado exitosamente con ID: {}", usaurioCreado.getCedula());

        return usaurioCreado;
    }
    /**
     * Buscar usuario por ID con manejo de errores
     */
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long cedula) {
        log.debug("Buscando usuario por ID: {}", cedula);

        return usuarioDAO.findById(cedula)
                .orElseThrow(() -> {
                    log.warn("usuario no encontrado con ID: {}", cedula);
                    return new RuntimeException("usuario no encontrado con ID: " + cedula);
                });
    }

    /**
     * Obtener todos los Usuarios
     */
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        log.debug("Obteniendo todos los usuario");
        return usuarioDAO.findAll();
    }

    /**
     * Eliminar usuarios con validaciones de negocio
     */
    public void deleteUsuario(Long cedula) {
        log.info("Eliminando usuarios ID: {}", cedula);

        // Verificar que el usuarios existe
        UsuarioDto usuario = findById(cedula);
        //Regla de negocio: No eliminar si tiene reservas
        List<ReservaDto> reservas = reservaService.findAll();
        for (ReservaDto reserva : reservas) {
            if (reserva.getCedula().equals(cedula)) {
                log.warn("Intento de eliminar usuario con reserva. ID: {}, reserva: {}",reserva.getIdReserva());
                throw new IllegalStateException(
                        String.format("No se puede eliminar el usuario porque tiene %d reservas(s) asociado(s)")
                );
            }
        }
        //Regla de negocio: No eliminar si tiene reporte
        List<ReporteDto> reportes = reporteService.findAll();
        for (ReporteDto reporte : reportes) {
            if (reporte.getCedula().equals(cedula)) {
                log.warn("Intento de eliminar usuario con reserva. ID: {}, reserva: {}",reporte.getIdReserva());
                throw new IllegalStateException(
                        String.format("No se puede eliminar el usuario porque tiene %d reservas(s) asociado(s)")
                );
            }
        }

        // Eliminar usuario
        boolean deleted = usuarioDAO.delete(cedula);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar usuarios con ID: " + cedula);
        }

        log.info("usuarios eliminado exitosamente ID: {}", cedula);
    }
    /**
     * Actualizar usuario con validaciones
     */
    public UsuarioDto update(Long cedula, UsuarioDto updateDTO) {
        log.info("Actualizando usaurio ID: {}", cedula);

        // Verificar que el usaurio existe
        if (!usuarioDAO.findById(cedula).isPresent()) {
            log.warn("Intento de actualizar usuarios inexistente ID: {}", cedula);
            throw new RuntimeException("usuarios no encontrado con ID: " + cedula);
        }

        // Validaciones de negocio
        validarUsuario(updateDTO);

        // Actualizar
        UsuarioDto updatedSeller = usuarioDAO.update(cedula, updateDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar usuario"));

        log.info("Usuario actualizado exitosamente ID: {}", cedula);
        return updatedSeller;
    }
    
    /**
     * METODO PRIVADO: Validar datos de creación
     */
    private void validarUsuario(UsuarioDto usuarioDto) {
        // Validar cédula
        if (usuarioDto.getCedula() == null || usuarioDto.getCedula() <= 0) {
            throw new IllegalArgumentException("La cédula es obligatoria y debe ser un número positivo");
        }

        // Validar nombre
        if (usuarioDto.getNombre() == null || usuarioDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (usuarioDto.getNombre().length() > 45) {
            throw new IllegalArgumentException("El nombre no puede exceder 45 caracteres");
        }

        // Validar apellido
        if (usuarioDto.getApellido() == null || usuarioDto.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del usuario es obligatorio");
        }
        if (usuarioDto.getApellido().length() > 45) {
            throw new IllegalArgumentException("El apellido no puede exceder 45 caracteres");
        }

        // Validar email
        if (usuarioDto.getEmail() == null || usuarioDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        if (usuarioDto.getEmail().length() > 45) {
            throw new IllegalArgumentException("El email no puede exceder 45 caracteres");
        }
        if (!isValidEmail(usuarioDto.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        // Validar rol
        if (usuarioDto.getRol() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        // Validar contraseña
        if (usuarioDto.getContraseña() == null || usuarioDto.getContraseña().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (usuarioDto.getContraseña().length() < 8 || usuarioDto.getContraseña().length() > 45) {
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 45 caracteres");
        }
        // Ejemplo: validar que tenga letras y números
        if (!usuarioDto.getContraseña().matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra y un número");
        }

        // Validar teléfono
        if (usuarioDto.getTelefono() == null || usuarioDto.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (usuarioDto.getTelefono().length() > 45) {
            throw new IllegalArgumentException("El teléfono no puede exceder 45 caracteres");
        }
        if (!usuarioDto.getTelefono().matches("^[0-9+\\- ]+$")) {
            throw new IllegalArgumentException("El teléfono solo puede contener números, espacios, '+' o '-'");
        }

        for (int i = 0; i <findAll().size(); i++) {
            if (findAll().get(i).getCedula().equals(usuarioDto.getCedula())) {
                throw new IllegalArgumentException("El cedula es ya esta registrado");
            }
            if (findAll().get(i).getEmail().equals(usuarioDto.getEmail())) {
                throw new IllegalArgumentException("El email es ya esta registrado");
            }
        }

        // Validar fecha de registro
        if (usuarioDto.getFechaRegistro() == null) {
            throw new IllegalArgumentException("La fecha de registro es obligatoria");
        }
        if (usuarioDto.getFechaRegistro().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de registro no puede ser en el futuro");
        }
    }

    /**
     * METODO PRIVADO: Validar formato de email básico
     */
    private boolean isValidEmail(String email) {
        // Validación básica de email
        return email.contains("@") && email.contains(".");
    }
}