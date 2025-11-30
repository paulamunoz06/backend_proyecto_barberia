package co.edu.unicauca.microservicio_catalogo_horario.Barberos.controladores;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services.IBarberoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbero")
public class BarberoRESTController {
    @Autowired
    private IBarberoService service;

    @GetMapping
    public ResponseEntity<List<BarberoDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<BarberoDTORespuesta>> listarActivos() {
        return ResponseEntity.ok(service.findActive());
    }

    @GetMapping("/servicio/{id}")
    public ResponseEntity<List<BarberoDTORespuesta>> listarServicio(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findService(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberoDTORespuesta> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BarberoDTORespuesta> crear(@RequestBody BarberoDTOPeticion barbero) {
        return ResponseEntity.ok(service.save(barbero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberoDTORespuesta> actualizar(@PathVariable String id, @RequestBody BarberoDTOPeticion barbero) {
        return ResponseEntity.ok(service.update(id, barbero));
    }

    @PutMapping("/{idBarbero}/ocupacion/{ocupacionNombre}")
    public ResponseEntity<BarberoDTORespuesta> actualizarOcupacion(@PathVariable String idBarbero, @PathVariable String ocupacionNombre) {
        return ResponseEntity.ok(service.updateOcupacion(idBarbero, ocupacionNombre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
