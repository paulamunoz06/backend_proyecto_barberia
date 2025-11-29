package co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias;

import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.estructura.CodigoError;
import lombok.Getter;

@Getter
public class EntidadNoExisteException extends RuntimeException {

    private final String llaveMensaje;
    private final String codigo;

    public EntidadNoExisteException(co.edu.unicauca.microservicio_identidad_acceso.Excepciones.estructura.CodigoError code) {
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