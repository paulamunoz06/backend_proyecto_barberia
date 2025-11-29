package co.edu.unicauca.microservicio_catalogo_horario.Servicios.controladores;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio")
public class ServicioRESTController {
    @Autowired
    private IServicioService service;

    @GetMapping
    public ResponseEntity<List<ServicioDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ServicioDTORespuesta>> listarCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByCategoria(id));
    }

    @GetMapping("/barbero/{id}")
    public ResponseEntity<List<ServicioDTORespuesta>> listarBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByIdBarbero(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ServicioDTORespuesta> crear(@RequestBody ServicioDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTORespuesta> actualizar(@PathVariable Integer id, @RequestBody ServicioDTOPeticion servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

