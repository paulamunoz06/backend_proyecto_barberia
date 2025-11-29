package co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "reserva_fecha", nullable = false)
    private LocalDate fechaReserva;

    @Column(nullable = true)
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
        turno.setReserva(this);
    }

    public void removerTurno(Turno turno) {
        turnos.remove(turno);
        turno.setReserva(null);
    }
}
