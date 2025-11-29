package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turno_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "servicio_id", nullable = false)
    private Integer servicioId;

    @Column(name = "barbero_id", length = 20, nullable = false)
    private String barberoId;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @Column(name = "turno_descripcion", length = 50, nullable = false)
    private String descripcion;

    @Column(name = "turno_fecha_inicio", nullable = false)
    private LocalTime fechaInicio;

    @Column(name = "turno_hora_inicio", nullable = false)
    private LocalTime HoraInicio;

    @Column(name = "turno_hora_fin", nullable = true)
    private LocalTime HoraFin;
}