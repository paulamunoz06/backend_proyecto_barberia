package co.edu.unicauca.microservicio_turnos_reservas.Excepciones;

import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.estructura.CodigoError;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.estructura.Error;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.estructura.ErrorUtils;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadYaExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;


@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericException(final HttpServletRequest req, final Exception ex, final Locale locale) {
        final Error error = ErrorUtils
                .crearError(CodigoError.ERROR_GENERICO.getCodigo(),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setUrl(req.getRequestURL().toString()).setMetodo(req.getMethod());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleIoException(final HttpServletRequest req, final RuntimeException ex, final Locale locale) {
        final Error error = ErrorUtils
                .crearError(CodigoError.ERROR_GENERICO.getCodigo(),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setUrl(req.getRequestURL().toString()).setMetodo(req.getMethod());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntidadYaExisteException.class)
    public ResponseEntity<Error> handleGenericException(final HttpServletRequest req, final EntidadYaExisteException ex) {
        final Error error = ErrorUtils
                .crearError(CodigoError.ENTIDAD_YA_EXISTE.getCodigo(),
                        ex.getLlaveMensaje(),
                        HttpStatus.NOT_ACCEPTABLE.value())
                .setUrl(req.getRequestURL().toString()).setMetodo(req.getMethod());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReglaNegocioExcepcion.class)
    public ResponseEntity<Error> handleGenericException(final HttpServletRequest req, final ReglaNegocioExcepcion ex, final Locale locale) {
        final Error error = ErrorUtils
                .crearError(CodigoError.VIOLACION_REGLA_DE_NEGOCIO.getCodigo(),
                        ex.getLlaveMensaje(),
                        HttpStatus.BAD_REQUEST.value())
                .setUrl(req.getRequestURL().toString()).setMetodo(req.getMethod());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntidadNoExisteException.class)
    public ResponseEntity<Error> handleGenericException(final HttpServletRequest req, final EntidadNoExisteException ex, final Locale locale) {
        final Error error = ErrorUtils
                .crearError(CodigoError.ENTIDAD_NO_ENCONTRADA.getCodigo(),
                        ex.getLlaveMensaje(),
                        HttpStatus.NOT_FOUND.value())
                .setUrl(req.getRequestURL().toString()).setMetodo(req.getMethod());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}

