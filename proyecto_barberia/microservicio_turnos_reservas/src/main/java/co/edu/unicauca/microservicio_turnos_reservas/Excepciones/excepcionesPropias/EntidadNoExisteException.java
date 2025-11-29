package co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias;

import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.estructura.CodigoError;
import lombok.Getter;

@Getter
public class EntidadNoExisteException extends RuntimeException {

    private final String llaveMensaje;
    private final String codigo;

    public EntidadNoExisteException(CodigoError code) {
        super(code.getCodigo());
        this.llaveMensaje = code.getLlaveMensaje();
        this.codigo = code.getCodigo();
    }

    public EntidadNoExisteException(final String message) {
        super(message);
        this.llaveMensaje = message;
        this.codigo = CodigoError.ENTIDAD_NO_ENCONTRADA.getCodigo();
    }
}