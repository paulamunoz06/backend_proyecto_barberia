package co.edu.unicauca.microservicio_turnos_reservas.Turnos.Observer;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;

public interface ObservadoresTurno {
    void notificar (Turno turno);
}
