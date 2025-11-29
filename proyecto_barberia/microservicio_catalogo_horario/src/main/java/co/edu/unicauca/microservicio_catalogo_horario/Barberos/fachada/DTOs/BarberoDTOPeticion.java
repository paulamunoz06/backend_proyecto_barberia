package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.EstadoUsuario;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class BarberoDTOPeticion {

    private String nombre;

    private String id;

    private LocalDate nacimiento;

    private List<Integer> servicios;

    private String estado;

    private Long telefono;

    private String email;

    private String fotografia;

    private String idAdministrador;

    private Integer ocupacion;

}
