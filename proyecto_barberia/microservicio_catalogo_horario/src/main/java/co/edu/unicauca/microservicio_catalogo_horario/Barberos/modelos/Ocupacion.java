package co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ocupacion")
public class Ocupacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocupacion_id")
    private Integer id;

    @Column(name = "ocupacion_nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "ocupacion", fetch = FetchType.LAZY)
    private List<Barbero> barberos;

    public Ocupacion(Integer id) {
        this.id = id;
    }
}