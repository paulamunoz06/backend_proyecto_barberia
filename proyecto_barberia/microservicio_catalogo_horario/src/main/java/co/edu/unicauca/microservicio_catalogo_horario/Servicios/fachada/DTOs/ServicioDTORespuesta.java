package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServicioDTORespuesta {
    private Integer servicioId;

    private Integer categoriaId;

    private String descripcion;

    private Integer duracion;

    private Float precio;

    private Integer preparacion;

    private String nombre;

}
