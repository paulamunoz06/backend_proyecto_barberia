package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTORespuesta {
  private String token;
  private String username;
  private String rol;
}
