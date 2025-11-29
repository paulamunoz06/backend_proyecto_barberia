package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "incidencia")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incidencia_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "tipoincidencia_id", nullable = false)
    private TipoIncidencia tipoIncidencia;

    @Column(name = "tipoincidencia_descripcion", length = 50, nullable = false)
    private String descripcion;
}