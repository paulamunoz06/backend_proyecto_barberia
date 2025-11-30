package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTORespuesta;

import java.util.List;

public interface AsignarBarbero {
    List<BarberoFranjaDTORespuesta> elegirBarbero(BarberoFranjaDTOPeticion peticion);
}
