package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class BarberoFranjaDTOPeticion {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "Los barberos que van a realizar los servicios es obligatorio")
    @NotEmpty(message = "Debe contener al menos un servicio con su barbero")
    private Map<@NotNull(message = "El id del servicio no puede ser nulo")
            Integer,
            @NotNull(message = "El id del barbero no puede ser nulo")
                    String> servicioBarberoIds;
}
