package co.edu.unicauca.microservicio_turnos_reservas.Turnos.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio")
public class TurnoRESTController {
    @Autowired
    private ITurnoService service;

    @GetMapping
    public ResponseEntity<List<TurnoDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/barbero/")
    public ResponseEntity<List<TurnoDTORespuesta>> listarBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoId(id));
    }

    @GetMapping("/reserva/")
    public ResponseEntity<List<TurnoDTORespuesta>> listarReserva(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByReservaId(id));
    }

    @GetMapping("/activas/barbero/")
    public ResponseEntity<List<TurnoDTORespuesta>> listarBarberoActivos(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoIdActivos(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TurnoDTORespuesta> crear(@RequestBody TurnoDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTORespuesta> actualizar(@PathVariable Integer id, @RequestBody TurnoDTOPeticion servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

