package co.edu.unicauca.microservicio_turnos_reservas.Turnos.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.IncidenciaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.IIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenciaRESTController {
    @Autowired
    private IIncidenciaService service;

    @GetMapping("/barbero/{id}")
    public ResponseEntity<List<IncidenciaDTORespuesta>> listarBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoId(id));
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<List<IncidenciaDTORespuesta>> listarReserva(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByReservaId(id));
    }

    @GetMapping("/turno/{id}")
    public ResponseEntity<List<IncidenciaDTORespuesta>> listarTurno(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByTurnoId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
}

