package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.State.ConfirmadoState;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.State.TurnoState;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.State.TurnoStateFactory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "turno_fecha", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "turno_hora_inicio", nullable = true)
    private LocalTime HoraInicio;

    @Column(name = "turno_hora_fin", nullable = true)
    private LocalTime HoraFin;

    @Transient
    private TurnoState state;

    @PostLoad
    public void inicializarEstado() {
        this.state = TurnoStateFactory.create(estado.getNombre());
        this.state.setContext(this);
    }

    public void cambiarEstado(TurnoState nuevoEstado) {
        this.state = nuevoEstado;
        nuevoEstado.setContext(this);
    }

    public void iniciar() { state.iniciar(); }
    public void cancelar() { state.cancelar(); }
    public void completar() { state.completar(); }
    public void marcarNoPresentado() { state.marcarNoPresentado(); }
}