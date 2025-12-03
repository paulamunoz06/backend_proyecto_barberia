package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.controlador;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services.UsuarioServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
  
  @Autowired
  UsuarioServiceImpl usuarioService;

  @PostMapping(value = "/barbero", consumes = "multipart/form-data")
  public ResponseEntity<?> registerBarbero(
          @Valid @ModelAttribute BarberoUsuarioDTOPeticion signUpRequest,
          @RequestParam("archivo") MultipartFile archivo) {
    return usuarioService.registerBarbero(signUpRequest, archivo);
  }

  @PutMapping(value = "/barbero/{id}/foto", consumes = "multipart/form-data")
  public ResponseEntity<?> actualizarFotoBarbero(
          @PathVariable String id,
          @RequestParam("archivo") MultipartFile archivo) {
    return usuarioService.actualizarFotoBarbero(id, archivo);
  }

  @DeleteMapping("/barbero/{id}/foto")
  public ResponseEntity<?> eliminarFoto(@PathVariable("id") String idBarbero) {
    return usuarioService.eliminarFotoBarbero(idBarbero);
  }

  @GetMapping("/barbero/foto/{nombre}")
  public ResponseEntity<?> verImagen(@PathVariable String nombre) {
    return usuarioService.visualizarFotoBarbero(nombre);
  }

  @PostMapping("/cliente")
  public ResponseEntity<?> registerCliente(@Valid @RequestBody ClienteUsuarioDTOPeticion signUpRequest) {
    return usuarioService.registerCiente(signUpRequest);
  }
}
