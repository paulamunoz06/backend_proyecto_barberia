package co.edu.unicauca.microservicio_catalogo_horario.Excepciones.estructura;

public final class ErrorUtils {

    ErrorUtils() {}

    public static Error crearError(final String codigoError, final String llaveMensaje, final Integer codigoHttp) {
        final Error error = new Error();
        error.setCodigoError(codigoError);
        error.setMensaje(llaveMensaje);
        error.setCodigoHttp(codigoHttp);
        return error;
    }
}