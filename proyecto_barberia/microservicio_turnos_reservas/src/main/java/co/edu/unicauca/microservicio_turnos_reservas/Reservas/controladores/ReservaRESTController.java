package co.edu.unicauca.microservicio_turnos_reservas.Reservas.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.servicios.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
public class ReservaRESTController {
    @Autowired
    private IReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservaDTORespuesta> crear(@RequestBody ReservaDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTORespuesta> actualizar(@PathVariable Integer id, @RequestBody ReservaDTORespuesta servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<Boolean> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancelar(id));
    }
}

