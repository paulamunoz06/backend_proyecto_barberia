package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TurnoDTORespuesta {
    private Integer id;
    private Integer reserva;
    private String cliente;
    private Integer servicioId;
    private String barberoId;
    private Integer estado;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
