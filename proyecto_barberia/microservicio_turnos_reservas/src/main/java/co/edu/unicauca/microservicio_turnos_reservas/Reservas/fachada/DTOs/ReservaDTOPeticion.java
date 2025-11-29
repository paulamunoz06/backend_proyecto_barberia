package co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservaDTOPeticion {
    private String cliente;
    private LocalDate fechaReserva;
    private List<TurnoDTOPeticion> turnos;
}
