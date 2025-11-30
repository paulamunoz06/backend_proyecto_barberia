package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.controlador;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
  
  @Autowired
  UsuarioServiceImpl usuarioService;

  @PostMapping("/barbero")
  public ResponseEntity<?> registerBarbero(@RequestBody BarberoUsuarioDTOPeticion signUpRequest) {
    return usuarioService.registerBarbero(signUpRequest);
  }

  @PostMapping("/cliente")
  public ResponseEntity<?> registerCliente(@RequestBody ClienteUsuarioDTOPeticion signUpRequest) {
    return usuarioService.registerCiente(signUpRequest);
  }
}
