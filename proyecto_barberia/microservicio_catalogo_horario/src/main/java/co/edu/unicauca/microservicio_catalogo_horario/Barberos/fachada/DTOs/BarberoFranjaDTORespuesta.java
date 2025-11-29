package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BarberoFranjaDTORespuesta {
    private List<String> barberoIds;
    private Map<LocalTime, LocalTime> franjas;
}
