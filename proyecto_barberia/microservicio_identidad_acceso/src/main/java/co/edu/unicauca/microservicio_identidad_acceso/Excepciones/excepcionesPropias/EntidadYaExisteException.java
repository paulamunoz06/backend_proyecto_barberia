package co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias;

import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.estructura.CodigoError;
import lombok.Getter;

@Getter
public class EntidadYaExisteException extends RuntimeException {

    private final String llaveMensaje;
    private final String codigo;

    public EntidadYaExisteException(co.edu.unicauca.microservicio_identidad_acceso.Excepciones.estructura.CodigoError code) {
        super(code.getCodigo());
        this.llaveMensaje = code.getLlaveMensaje();
        this.codigo = code.getCodigo();
    }

    public EntidadYaExisteException(final String message) {
        super(message);
        this.llaveMensaje = message;
        this.codigo = CodigoError.ENTIDAD_YA_EXISTE.getCodigo();
    }
}
