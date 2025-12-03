package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTOSolicitud {

  @NotNull(message = "El nombre de usuario es obligatorio")
  private String username;

  @NotNull(message = "La contraseña es obligatoria")
  private String password;
}
