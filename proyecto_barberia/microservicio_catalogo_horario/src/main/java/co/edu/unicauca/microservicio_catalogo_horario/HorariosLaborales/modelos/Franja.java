package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "franja",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                    "horario_id",
                    "franja_inicio",
                    "franja_fin"
            }
        )
    }
)
public class Franja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "franja_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @Column(name = "franja_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "franja_fin", nullable = false)
    private LocalTime horaFin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trabajo",
            joinColumns = @JoinColumn(name = "franja_id"),
            inverseJoinColumns = @JoinColumn(name = "barbero_id")
    )
    private List<Barbero> barberos;
}