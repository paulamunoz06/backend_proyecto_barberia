package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class BarberoUsuarioDTOPeticion {

    @NotNull(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El ID del barbero es obligatorio")
    private String id;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate nacimiento;

    @NotEmpty(message = "Debe registrar al menos un servicio")
    private List<Integer> servicios;

    @NotNull(message = "El teléfono es obligatorio")
    @Positive(message = "El teléfono debe ser un número válido")
    private Long telefono;

    @NotNull(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    private String fotografia;

    @NotNull(message = "El idAdministrador es obligatorio")
    private String idAdministrador;

    @NotNull(message = "La contraseña es obligatoria")
    private String password;
}