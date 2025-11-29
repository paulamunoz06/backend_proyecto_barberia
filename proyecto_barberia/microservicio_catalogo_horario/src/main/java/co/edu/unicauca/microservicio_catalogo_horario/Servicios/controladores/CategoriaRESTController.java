package co.edu.unicauca.microservicio_catalogo_horario.Servicios.controladores;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.CategoriaDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaRESTController {
    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/categorias")
    public List<CategoriaDTORespuesta> listarCategorias() {
        return categoriaService.findAll();
    }

}
