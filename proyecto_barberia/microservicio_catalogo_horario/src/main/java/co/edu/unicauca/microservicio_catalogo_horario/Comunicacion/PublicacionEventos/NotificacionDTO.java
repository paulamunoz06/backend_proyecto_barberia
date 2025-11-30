package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificacionDTO {
    private String correo;
    private String mensaje;
}