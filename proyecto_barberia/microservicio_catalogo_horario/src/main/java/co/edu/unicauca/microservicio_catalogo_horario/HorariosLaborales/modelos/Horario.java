package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Administrador;
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
@Table(name = "horario")
public class Horario {

    @Id
    @Column(name = "horario_id")
    private LocalDate id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;

    @OneToMany(mappedBy = "horario",fetch = FetchType.LAZY)
    private List<Franja> franjas;
}