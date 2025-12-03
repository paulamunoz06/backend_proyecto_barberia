package co.edu.unicauca.microservicio_turnos_reservas.Cliente.controladores;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.servicios.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteRESTController {
    @Autowired
    private IClienteService service;

    @GetMapping
    public ResponseEntity<List<ClienteDTORespuesta>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTORespuesta> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/correo")
    public ResponseEntity<String> buscarCorreo(@PathVariable String id) {
        return ResponseEntity.ok(service.obtenerCorreoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteDTORespuesta> crear(@Valid @RequestBody ClienteDTOPeticion servicio) {
        return ResponseEntity.ok(service.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTORespuesta> actualizar(@PathVariable String id, @Valid @RequestBody ClienteDTOPeticion servicio) {
        return ResponseEntity.ok(service.update(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

