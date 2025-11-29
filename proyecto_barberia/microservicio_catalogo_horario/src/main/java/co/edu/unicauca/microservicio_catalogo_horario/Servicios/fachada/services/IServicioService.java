package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;

import java.util.List;

public interface IServicioService {
    List<ServicioDTORespuesta> findAll();

    ServicioDTORespuesta findById(Integer id);

    List<ServicioDTORespuesta> findByCategoria(Integer id);

    List<ServicioDTORespuesta> findByIdBarbero(String id);

    ServicioDTORespuesta save(ServicioDTOPeticion servicio);

    ServicioDTORespuesta update(Integer id, ServicioDTOPeticion servicio);

    boolean delete(Integer id);

    Servicio findByIdInt(Integer id);
}

