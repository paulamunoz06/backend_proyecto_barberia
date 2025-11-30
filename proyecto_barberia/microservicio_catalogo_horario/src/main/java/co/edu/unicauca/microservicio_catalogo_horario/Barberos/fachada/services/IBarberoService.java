package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;

import java.util.List;

public interface IBarberoService {
    List<BarberoDTORespuesta> findAll();

    List<BarberoDTORespuesta> findActive();

    List<BarberoDTORespuesta> findService(Integer servicioId);

    BarberoDTORespuesta findById(String id);

    Barbero findByIdInt(String id);

    boolean barberoHaceServicio(String barberoId, Integer servicioId);

    BarberoDTORespuesta save(BarberoDTOPeticion barbero);

    BarberoDTORespuesta update(String id, BarberoDTOPeticion barbero);

    BarberoDTORespuesta updateOcupacion(String id, String ocupacionId);

    boolean delete(String id);


}
