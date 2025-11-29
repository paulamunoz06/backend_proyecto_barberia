package co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTOPeticion {
    private String id;
    private String nombre;
    private String email;
    private Long telefono;
    private String estado;
}
