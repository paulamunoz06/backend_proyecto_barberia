package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServicioDTOPeticion {

    private Integer categoriaId;

    private String administradorId;

    private String descripcion;

    private Integer duracion;

    private Float precio;

    private Integer preparacion;

    private String nombre;

}
