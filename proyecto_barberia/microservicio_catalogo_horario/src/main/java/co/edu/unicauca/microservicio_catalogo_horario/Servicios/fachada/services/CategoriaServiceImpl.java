package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.CategoriaServicioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.CategoriaDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.CategoriaServicio;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CategoriaServiceImpl implements ICategoriaService{
    @Autowired
    private CategoriaServicioRepository repo;

    @Override
    public List<CategoriaDTORespuesta> findAll() {
        List<CategoriaServicio> categorias = repo.findAll();
        List<CategoriaDTORespuesta> respuesta = new ArrayList<>();

        for (CategoriaServicio s : categorias) {
            respuesta.add(mapearARespuesta(s));
        }

        return respuesta;
    }

    private CategoriaDTORespuesta mapearARespuesta(CategoriaServicio s) {
        CategoriaDTORespuesta dto = new CategoriaDTORespuesta();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        return dto;
    }
}

