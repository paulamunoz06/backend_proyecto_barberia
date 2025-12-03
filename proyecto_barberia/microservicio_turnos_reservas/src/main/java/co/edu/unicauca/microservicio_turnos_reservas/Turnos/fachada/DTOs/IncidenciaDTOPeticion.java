package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaDTOPeticion {

    @NotNull(message = "El turno es obligatorio")
    private Integer turno;

    @NotNull(message = "El tipo de incidencia es obligatorio")
    private Integer tipoIncidencia;

    @NotNull(message = "La descripción es obligatoria")
    private String descripcion;
}
