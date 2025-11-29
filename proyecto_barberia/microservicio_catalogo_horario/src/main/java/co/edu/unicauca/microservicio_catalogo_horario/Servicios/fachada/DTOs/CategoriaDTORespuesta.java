package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTORespuesta {
    private Integer id;
    private String nombre;
}