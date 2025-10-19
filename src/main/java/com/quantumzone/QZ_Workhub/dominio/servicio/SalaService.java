package com.quantumzone.QZ_Workhub.dominio.servicio;
import com.quantumzone.QZ_Workhub.dominio.dto.ReservaDto;
import com.quantumzone.QZ_Workhub.dominio.dto.SalaDto;
import com.quantumzone.QZ_Workhub.persistencia.dao.SalaDAO;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Implementación del servicio de salas
 *
 * ANOTACIONES:
 * @Service - Marca como componente de servicio de Spring
 * @Transactional - Manejo automático de transacciones
 * @RequiredArgsConstructor - Lombok genera constructor con dependencias final
 * @Slf4j - Lombok genera logger automáticamente
 *
 * PRINCIPIOS APLICADOS:
 * - Inversión de Dependencias: Depende de salaDAO (abstracción)
 * - Single Responsibility: Solo lógica de negocio de salas
 * - Fail Fast: Validaciones tempranas y excepciones claras
 */
@Service
@Transactional
@Slf4j
public class SalaService {

    private final SalaDAO  salaDAO;
    private final ReservaService reservaService;

    @Autowired
    public SalaService(SalaDAO  salaDAO,  ReservaService reservaService) {
        this.salaDAO = salaDAO;
        this.reservaService = reservaService;

        // Inicializamos algunos datos si es necesario
        initSampleData();
    }

    private void initSampleData() {
        // Aquí puedes cargar datos iniciales de prueba si deseas
    }

    // Guardar una sala
    public SalaDto save(SalaDto salaDto) {

        log.info("Creando nueva sala con ID: {}", salaDto.getIdSala());
        if (salaDAO.findById(salaDto.getIdSala()).isPresent()) {
            log.warn ("intento crear una sala con un ID duplicado: {}", salaDto.getIdSala());
            throw new IllegalArgumentException("La sala ya existe:" + salaDto.getIdSala());
        }
        validarSala(salaDto);

        SalaDto salaCreada = salaDAO.save(salaDto);
        log.info("Sala con ID creada: {}", salaCreada.getIdSala());
        return salaCreada;
    }

    /**
     * Buscar sala por ID con manejo de errores
     */
    @Transactional(readOnly = true)
    public SalaDto findById(Long id){
        log.info("Buscando sala con ID: {}", id);

        return salaDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro la sala con ID: {}", id);
                    return new RuntimeException("No se encontro la sala con ID: " + id);
                });
    }

    /**
     * Obtener todas las salas
     */
    @Transactional(readOnly = true)
    public List<SalaDto> findAll(){
        log.info( "Buscando salas");
        return salaDAO.findAll();
    }

    /**
     * Eliminar salas con validaciones de negocio
     */
    public void deleteSala(Long id){
        log.info("Eliminando sala con ID: {}", id);

        //verificar que la sala si existe
        SalaDto salaBusacada = findById(id);

        //Regla de negocio: No eliminar si tiene reservas
        List<ReservaDto> reservas = reservaService.findAll();
        for (ReservaDto reserva : reservas) {
            if (reserva.getIdReserva().equals(id)) {
                log.warn("Intento de eliminar usuario con reserva. ID: {}, reserva: {}",reserva.getIdReserva());
                throw new IllegalStateException(
                        String.format("No se puede eliminar el usuario porque tiene %d reservas(s) asociado(s)", id)
                );
            }
        }
         // Eliminar sala
        boolean eliminar = salaDAO.delete(id);
        if (!eliminar) {
            throw new RuntimeException("No se logro eliminar la sala con ID: " + id);
        }

        log.info("Sala con ID: {}", salaBusacada.getIdSala() + "eliminado exitosamente");
    }

    /**
     * Actualizar sala con validaciones
     */
    public SalaDto update(Long id, SalaDto salaDto) {
        log.info("Actualizando sala con ID: {}", id);

        //verificamos que la sala exista
        if(!salaDAO.findById(id).isPresent()) {
            log.warn("No se encontro la sala con ID: {}", id);
            throw new IllegalArgumentException("No se encontro la sala con ID: " + id);
        }

        // validaciones de negocio
        validarSala(salaDto);

        // Actualizar
        SalaDto salaActualizada = salaDAO.update(id, salaDto)
                .orElseThrow(() -> new RuntimeException("Error al actualizar la sala con ID: " + id));
        log.info("Sala actualizada: {}", salaActualizada);
        return salaActualizada;
    }

    /**
     * Validar los datos para crear y/o actualizar la sala
     */
    private void validarSala(SalaDto salaDto) {
        //validar ID
        if(salaDto.getIdSala() == null || salaDto.getIdSala() <= 0) {
            throw new IllegalArgumentException("La id es obligatoria y no puede ser negativa");
        }

        //validar nombre
        if(salaDto.getNombre() == null || salaDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if(salaDto.getNombre().length() > 45) {
            throw new IllegalArgumentException("El nombre de la sala no puede exceder los 45 caracteres");
        }

        //validar capacidad
        if(salaDto.getCapacidad() <= 0 || salaDto.getCapacidad() == null) {
            throw new IllegalArgumentException("La sala debe tener alguna capacidad fija mayor a 0");
        }

        //validar peso
        if(salaDto.getPrecio() <= 0 || salaDto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }

        //validar descripcion
        if(salaDto.getDescripcion() == null || salaDto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatorio");
        }
        if(salaDto.getDescripcion().length() >  45) {
            throw new IllegalArgumentException("La descripcion no puede exceder los 45 caracteres");
        }
    }

}
