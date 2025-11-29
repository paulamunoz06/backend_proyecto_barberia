package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTOSolicitud {
  private String username;
  private String password;
}
