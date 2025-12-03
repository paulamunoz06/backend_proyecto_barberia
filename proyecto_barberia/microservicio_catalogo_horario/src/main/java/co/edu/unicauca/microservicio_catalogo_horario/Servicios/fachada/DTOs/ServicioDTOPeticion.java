package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServicioDTOPeticion {

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;

    @NotNull(message = "El ID del administrador es obligatorio")
    private String administradorId;

    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 250, message = "La descripción no puede superar 250 caracteres")
    private String descripcion;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser mayor a 0")
    private Integer duracion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Float precio;

    @NotNull(message = "El tiempo de preparación es obligatorio")
    @Positive(message = "La preparación debe ser mayor a 0")
    private Integer preparacion;

    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
}
