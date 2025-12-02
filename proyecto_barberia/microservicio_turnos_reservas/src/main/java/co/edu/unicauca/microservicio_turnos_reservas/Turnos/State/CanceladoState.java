package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

public class CanceladoState extends TurnoState {
    @Override
    public String getNombre() {
        return "CANCELADO";
    }
}
