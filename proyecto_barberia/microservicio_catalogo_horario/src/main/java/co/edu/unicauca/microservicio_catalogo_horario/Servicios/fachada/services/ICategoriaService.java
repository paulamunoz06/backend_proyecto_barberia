package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.CategoriaDTORespuesta;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaDTORespuesta> findAll();
}
