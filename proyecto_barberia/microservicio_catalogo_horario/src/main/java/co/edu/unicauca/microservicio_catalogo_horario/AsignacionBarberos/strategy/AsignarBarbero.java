package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.strategy;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoFranjaDTORespuesta;

import java.util.List;

public interface AsignarBarbero {
    List<BarberoFranjaDTORespuesta> elegirBarbero(BarberoFranjaDTOPeticion peticion);
}
