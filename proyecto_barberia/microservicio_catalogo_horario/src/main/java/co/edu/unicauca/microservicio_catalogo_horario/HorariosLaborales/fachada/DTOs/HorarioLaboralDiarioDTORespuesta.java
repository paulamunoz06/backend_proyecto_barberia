package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class HorarioLaboralDiarioDTORespuesta {
    private LocalDate id;
    private List<FranjaHorarioDTORespuesta> franjas;
}
