package co.edu.unicauca.microservicio_identidad_acceso.Excepciones.estructura;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodigoError {

    ERROR_GENERICO("GC-0001", "Error generico"),
    ENTIDAD_YA_EXISTE("GC-0002", "Entidad existente"),
    ENTIDAD_NO_ENCONTRADA("GC-0003", "Entidad no encontrada"),
    VIOLACION_REGLA_DE_NEGOCIO("GC-0004", "Regla de negocio violada"),
    NO_VALIDACION("GC-0005", "Acceso no validado"),
    NO_CREDENTIALS("GC-0005", "Error al verificar credenciales");

    private final String codigo;
    private final String llaveMensaje;
}
