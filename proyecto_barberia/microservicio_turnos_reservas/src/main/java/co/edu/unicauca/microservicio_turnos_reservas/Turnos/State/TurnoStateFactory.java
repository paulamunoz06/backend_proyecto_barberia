package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

public class TurnoStateFactory {
    public static TurnoState create(String nombre) {
        return switch (nombre) {
            case "CONFIRMADO" -> new ConfirmadoState();
            case "EN_CURSO" -> new EnCursoState();
            case "CANCELADO" -> new CanceladoState();
            case "COMPLETADO" -> new CompletadoState();
            case "NO_PRESENTADO" -> new NoPresentadoState();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombre);
        };
    }
}
