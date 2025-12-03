package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.controladores;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services.IBarberoService;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services.IFranjaHorarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/franja")
public class FranjaHorarioRESTController {
    @Autowired
    private IFranjaHorarioService service;

    @GetMapping("/horario/{id}")
    public ResponseEntity<List<FranjaHorarioDTORespuesta>> buscarHorario(@PathVariable LocalDate id) {
        return ResponseEntity.ok(service.findHorario(id));
    }

    @GetMapping("/barbero/{id}")
    public ResponseEntity<List<FranjaHorarioDTORespuesta>> buscarBarbero(@PathVariable String id) {
        return ResponseEntity.ok(service.findBarbero(id));
    }

    @GetMapping("/barbero/{id}/{horaInicioBusqueda}")
    public ResponseEntity<List<FranjaHorarioDTORespuesta>> buscarBarberoDesdeHora(@PathVariable String id, @PathVariable LocalDateTime horaInicioBusqueda) {
        return ResponseEntity.ok(service.findBarberoDesdeHora(id, horaInicioBusqueda.toLocalDate(), horaInicioBusqueda.toLocalTime()));
    }

    @PostMapping("/barbero/{id}/{horaInicioBusqueda}")
    public ResponseEntity<Boolean> verificarBarberoDesdeHora(@PathVariable String id, @PathVariable LocalDateTime horaInicioBusqueda) {
        return ResponseEntity.ok(service.verificarBarberoDesdeHora(id, horaInicioBusqueda.toLocalDate(), horaInicioBusqueda.toLocalTime()));
    }

    @GetMapping("/barbero/{id}/{fecha}/{inicio}/{fin}")
    public ResponseEntity<Boolean> duracionContinua(@PathVariable String id, @PathVariable LocalDate fecha, @PathVariable LocalTime inicio, @PathVariable LocalTime fin) {
        return ResponseEntity.ok(service.tieneDuracionContinua(id,fecha,inicio,fin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FranjaHorarioDTORespuesta> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<FranjaHorarioDTORespuesta> crear(@Valid @RequestBody FranjaHorarioDTOPeticion franja) {
        return ResponseEntity.ok(service.save(franja));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FranjaHorarioDTORespuesta> actualizar(@PathVariable Integer id, @Valid @RequestBody FranjaHorarioDTORespuesta franja) {
        return ResponseEntity.ok(service.update(id,franja));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
