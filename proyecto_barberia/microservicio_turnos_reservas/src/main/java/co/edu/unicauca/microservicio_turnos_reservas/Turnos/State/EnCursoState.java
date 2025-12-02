package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

public class EnCursoState extends TurnoState {

    @Override
    public void completar() {
        turno.cambiarEstado(new CompletadoState());
    }

    @Override
    public void cancelar() {
        turno.cambiarEstado(new CanceladoState());
    }

    @Override
    public String getNombre() {
        return "EN_CURSO";
    }
}
