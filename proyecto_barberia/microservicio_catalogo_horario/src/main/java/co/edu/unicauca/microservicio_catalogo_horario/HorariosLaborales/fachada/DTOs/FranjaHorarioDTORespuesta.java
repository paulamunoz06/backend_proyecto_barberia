package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
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
    private Integer id;

    private LocalDate horarioId;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private List<String> barberos;
}
