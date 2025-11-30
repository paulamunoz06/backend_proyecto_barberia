package co.edu.unicauca.microservicio_turnos_reservas.Turnos.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.ITurnoService;
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

    @GetMapping("/activas/barbero/{id}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarTurnosActivosPorBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findByBarberoIdActivos(id));
    }

    @GetMapping("/api/turno/activas/barbero/{barberoId}/{fecha}")
    public ResponseEntity<List<TurnoDTORespuesta>> listarTurnosActivosPorBarberoFecha(@PathVariable String barberoId, @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(service.findByBarberoAndFecha(barberoId,fecha));
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
    public ResponseEntity<TurnoDTORespuesta> crear(@RequestBody TurnoDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTORespuesta> actualizar(@PathVariable Integer id, @RequestBody TurnoDTORespuesta servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @PutMapping("/{idTurno}/estado/{idEstado}")
    public ResponseEntity<TurnoDTORespuesta> actualizarEstado(@PathVariable Integer idTurno, @PathVariable Integer idEstado) {
        return ResponseEntity.ok(service.updateEstado(idTurno, idEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
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

