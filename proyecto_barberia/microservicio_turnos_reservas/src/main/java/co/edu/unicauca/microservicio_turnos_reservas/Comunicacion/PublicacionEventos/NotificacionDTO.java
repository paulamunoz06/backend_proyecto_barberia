package co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificacionDTO {
    private String titulo;
    private String mensaje;
}