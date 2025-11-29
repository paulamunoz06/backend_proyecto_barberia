package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.controladores;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services.IFranjaHorarioService;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services.IHorarioLaboralDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class HorarioLaboralRESTController {
    @Autowired
    private IHorarioLaboralDiarioService service;

    @GetMapping("/{id}")
    public HorarioLaboralDiarioDTORespuesta buscar(@PathVariable LocalDate id) {
        return service.findById(id);
    }

    @GetMapping("/{idHorario}/{idBarbero}")
    public ResponseEntity<List<FranjaHorarioDTORespuesta>> buscarBarbero(@PathVariable LocalDate idHorario, @PathVariable String idBarbero) {
        return ResponseEntity.ok(service.findByBarbero(idHorario,idBarbero));
    }

    @PostMapping
    public HorarioLaboralDiarioDTORespuesta crear(@RequestBody HorarioLaboralDiarioDTOPeticion horario) {
        return service.save(horario);
    }

    @PutMapping("/{id}")
    public HorarioLaboralDiarioDTORespuesta actualizar(@PathVariable LocalDate id, @RequestBody HorarioLaboralDiarioDTOPeticion horario) {
        return service.update(id,horario);
    }
}
