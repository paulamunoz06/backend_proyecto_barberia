package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class NotificacionEvento {
    private String correoCliente;
    private String nombreBarbero;
    private String nombreServicio;
    private String fecha;
}