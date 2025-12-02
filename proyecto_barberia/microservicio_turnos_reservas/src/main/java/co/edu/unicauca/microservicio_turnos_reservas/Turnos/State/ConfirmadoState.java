package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

public class ConfirmadoState extends TurnoState {

    @Override
    public void iniciar() {
        turno.cambiarEstado(new EnCursoState());
    }

    @Override
    public void cancelar() {
        turno.cambiarEstado(new CanceladoState());
    }

    @Override
    public void marcarNoPresentado() {
        turno.cambiarEstado(new EnCursoState());
    }

    @Override
    public String getNombre() {
        return "CONFIRMADO";
    }
}
