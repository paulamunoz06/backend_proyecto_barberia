package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.controlador;

import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.ValidacionDTORespuesta;
import co.edu.unicauca.microservicio_identidad_acceso.Seguridad.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.LoginDTOSolicitud;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.services.AutenticacionServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {
  
  @Autowired
  AutenticacionServiceImpl objAuthImpl;

  @PostMapping
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTOSolicitud loginRequest) {
    return ResponseEntity.ok(this.objAuthImpl.authenticateUser(loginRequest));
  }

  @GetMapping("/validate-access")
  public ResponseEntity<ValidacionDTORespuesta> validateAccess(
          @RequestParam String path,
          @RequestParam(defaultValue = "GET") String method,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(this.objAuthImpl.validarAcceso(path, method, userDetails));
  }
}
