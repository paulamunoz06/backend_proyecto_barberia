package co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservaDTORespuesta {
    @NotNull(message = "El id del cliente es obligatorio")
    private Integer id;

    @NotNull(message = "El campo cliente es obligatorio")
    private String cliente;

    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDate fechaReserva;

    @NotEmpty(message = "Debe existir al menos un turno")
    private List<@NotNull TurnoDTORespuesta> turnos;
}