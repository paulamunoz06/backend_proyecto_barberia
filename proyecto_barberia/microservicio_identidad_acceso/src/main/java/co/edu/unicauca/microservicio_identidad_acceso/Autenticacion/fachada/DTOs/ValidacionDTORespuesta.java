package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidacionDTORespuesta {
    private boolean authorized;
    private List<String> roles;
}
