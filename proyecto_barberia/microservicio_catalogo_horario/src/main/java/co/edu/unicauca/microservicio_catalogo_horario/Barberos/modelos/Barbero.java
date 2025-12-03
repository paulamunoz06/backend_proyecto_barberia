package co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Administrador;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "barbero")
public class Barbero {
    @Id
    @Column(name = "barbero_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocupacion_id", nullable = true)
    private Ocupacion ocupacion;

    @Column(name = "barbero_nombre", nullable = false)
    private String nombre;

    @Column(name = "barbero_email", nullable = false)
    private String email;

    @Column(name = "barbero_telefono", nullable = false)
    private Long telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "barbero_estado", nullable = false)
    private EstadoUsuario estado;

    @Column(name = "barbero_nacimiento", nullable = false)
    private LocalDate nacimiento;

    @Column(name = "barbero_fotografia", nullable = true)
    private String fotografia;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "especializacion",
            joinColumns = @JoinColumn(name = "barbero_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private List<Servicio> serviciosEspecializados;

    @ManyToMany(mappedBy = "barberos", fetch = FetchType.LAZY)
    private List<Franja> trabajo;
}
