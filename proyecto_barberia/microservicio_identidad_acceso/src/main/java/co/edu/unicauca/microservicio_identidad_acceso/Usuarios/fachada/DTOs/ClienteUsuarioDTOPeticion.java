package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUsuarioDTOPeticion {
    @NotNull(message = "El id del cliente es obligatorio")
    private String id;

    @NotNull(message = "El nombre del cliente es obligatorio")
    private String nombre;

    @NotNull(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String email;

    @NotNull(message = "El teléfono es obligatorio")
    @Positive(message = "El teléfono debe ser un número válido")
    private Long telefono;

    @NotNull(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "La contraseña es obligatoria")
    private String password;
}