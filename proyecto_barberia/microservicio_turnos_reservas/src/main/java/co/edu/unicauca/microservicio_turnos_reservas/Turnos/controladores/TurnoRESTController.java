package co.edu.unicauca.microservicio_turnos_reservas.Turnos.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TipoIncidenciaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.ITurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turno")
public class TurnoRESTController {
    @Autowired
    private ITurnoService service;

    @GetMapping
    public ResponseEntity<List<TurnoDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/barbero/{id}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoId(id));
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarReserva(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByReservaId(id));
    }

    @GetMapping("/activas/barbero/{barberoId}/{fecha}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarTurnosActivosPorBarberoFecha(@PathVariable String barberoId, @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(service.findByBarberoAndFecha(barberoId,fecha));
    }

    @GetMapping("/activas/barbero/{id}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarTurnosActivosPorBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoIdActivos(id));
    }

    @GetMapping("/activas/servicio/{id}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarTurnosActivosPorServicio(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByServicioIdActivos(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TurnoDTORespuesta> crear(@Valid @RequestBody TurnoDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTORespuesta> actualizar(@PathVariable Integer id, @Valid @RequestBody TurnoDTORespuesta servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @PutMapping("/iniciar/{id}")
    public ResponseEntity<TurnoDTORespuesta> iniciarTurno(@PathVariable Integer id) {
        return ResponseEntity.ok(service.iniciarTurno(id));
    }

    @PutMapping("/completar/{id}")
    public ResponseEntity<TurnoDTORespuesta> completarTurno(@PathVariable Integer id) {
        return ResponseEntity.ok(service.completarTurno(id));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<TurnoDTORespuesta> cancelarTurno(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancelarTurno(id));
    }

    @PutMapping("/noPresentado/{id}")
    public ResponseEntity<TurnoDTORespuesta> clienteNoPresentado(@PathVariable Integer id) {
        return ResponseEntity.ok(service.marcarNoPresentado(id));
    }

    @GetMapping("/barbero/{barberoId}/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidadBarbero(
            @PathVariable String barberoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {
        return ResponseEntity.ok(service.verificarDisponibilidadBarbero(barberoId, fecha, horaInicio, horaFin));
    }
}

