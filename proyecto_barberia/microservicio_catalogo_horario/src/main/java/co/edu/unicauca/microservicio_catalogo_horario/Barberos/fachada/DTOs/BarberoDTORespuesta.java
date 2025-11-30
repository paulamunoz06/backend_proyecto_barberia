package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.EstadoUsuario;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Ocupacion;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Administrador;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class BarberoDTORespuesta {

    private String id;

    private String ocupacion;

    private String nombre;

    private String email;

    private Long telefono;

    private String estado;

    private LocalDate nacimiento;

    private String fotografia;

    private List<Integer> servicios;

}
