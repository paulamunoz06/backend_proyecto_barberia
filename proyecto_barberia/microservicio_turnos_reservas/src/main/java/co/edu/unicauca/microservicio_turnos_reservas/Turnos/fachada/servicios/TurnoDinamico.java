package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TurnoDinamico {
    private LocalTime encontrarHora(Turno turno) {
        return LocalTime.now();
    }
}
