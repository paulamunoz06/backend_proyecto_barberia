package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BarberoFranjaDTORespuesta {
    private Map<Integer,String> servicioBarbero;
    private Map<LocalTime, LocalTime> franjas;
}
