package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaDTORespuesta {
    private Integer id;
    private Integer turno;
    private Integer tipoIncidencia;
    private String descripcion;
}
