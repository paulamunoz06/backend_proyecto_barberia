package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.Observer.ObservadoresTurno;

import java.util.List;

public class TurnoObservable {
    private List<ObservadoresTurno> observadores;

    public void registrarObservador(ObservadoresTurno observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(ObservadoresTurno observador) {
        observadores.remove(observador);
    }

    public void notificarObservador(Turno turno) {
        for (ObservadoresTurno obs : observadores) {
            obs.notificar(turno);
        }
    }
}
