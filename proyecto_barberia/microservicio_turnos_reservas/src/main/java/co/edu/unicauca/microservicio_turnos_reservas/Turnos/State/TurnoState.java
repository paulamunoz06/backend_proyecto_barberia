package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;

public abstract class TurnoState {

    protected Turno turno;

    public void setContext(Turno turno) {
        this.turno = turno;
    }

    public void iniciar() {
        throw new IllegalStateException("No se puede iniciar el turno desde el estado " + turno.getEstado().toString());
    }

    public void completar() {
        throw new IllegalStateException("No se puede completar el turno desde el estado " + turno.getEstado().toString());
    }

    public void cancelar() {
        throw new IllegalStateException("No se puede cancelar el turno desde el estado " + turno.getEstado().toString());
    }

    public void marcarNoPresentado() {
        throw new IllegalStateException("No se puede marcar como no presentado desde el estado " + turno.getEstado().toString());
    }

    public abstract String getNombre();
}