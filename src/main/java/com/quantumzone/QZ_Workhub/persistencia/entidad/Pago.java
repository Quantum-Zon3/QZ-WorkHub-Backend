package com.quantumzone.QZ_Workhub.persistencia.entidad;
import java.time.LocalDateTime;
// imports de la persitencia
import com.quantumzone.QZ_Workhub.dominio.enums.EstadoPago;
import com.quantumzone.QZ_Workhub.dominio.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING) // guarda el nombre del enum en la BD
    @Column(name = "metodo_pago", nullable = false, length = 45)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago;

    @OneToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "idReserva")
    private Reserva reserva;
}
