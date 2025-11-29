package co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Horario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrador_id")
    private String id;

    @Column(name = "administrador_nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "administrador", fetch = FetchType.LAZY)
    private List<Barbero> barberos;

    @OneToMany(mappedBy = "administrador", fetch = FetchType.LAZY)
    private List<Servicio> servicios;

    @OneToMany(mappedBy = "administrador", fetch = FetchType.LAZY)
    private List<Horario> horariosDiarios;

    public Administrador(String nombre) {
        this.nombre = nombre;
        this.servicios = new ArrayList<>();
        this.barberos = new ArrayList<>();
        this.horariosDiarios = new ArrayList<>();
    }
}