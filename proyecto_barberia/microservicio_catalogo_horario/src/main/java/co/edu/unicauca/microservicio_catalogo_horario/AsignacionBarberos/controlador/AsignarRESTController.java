package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.controlador;

import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.services.AsignarBarberoService;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTORespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignar")
public class AsignarRESTController {
    @Autowired
    private AsignarBarberoService service;

    @GetMapping
    public ResponseEntity<List<BarberoFranjaDTORespuesta>> definirHorario(@RequestBody BarberoFranjaDTOPeticion peticion) {
        return ResponseEntity.ok(service.ejecutarEstrategia(peticion));
    }
}
