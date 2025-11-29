package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BarberoFranjaDTOPeticion {
    private LocalDate fecha;
    private Map<Integer, String> servicioBarberoIds;
}
