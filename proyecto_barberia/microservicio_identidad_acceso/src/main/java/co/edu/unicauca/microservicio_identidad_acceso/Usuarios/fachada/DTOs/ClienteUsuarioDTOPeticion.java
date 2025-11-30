package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUsuarioDTOPeticion {
    private String id;
    private String nombre;
    private String email;
    private Long telefono;
    private String estado;
    private String password;
}
