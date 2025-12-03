package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TurnoDTOPeticion {

    private Integer reserva;

    @NotNull(message = "El cliente es obligatorio")
    private String cliente;

    @NotNull(message = "El servicio es obligatorio")
    private Integer servicioId;

    @NotNull(message = "El barberoId es obligatorio")
    private String barberoId;

    @NotNull(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fechaInicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalTime horaInicio;

    private LocalTime horaFin;
}
