package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
public class FranjaHorarioDTORespuesta {
    @NotNull(message = "El id de la franja es obligatoria")
    private Integer id;

    @NotNull(message = "La fecha (id) del horario es obligatorio")
    private LocalDate horarioId;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "La lista de barberos no puede ser nula, puede enviarse vacía")
    @Valid
    private List<String> barberos;
}