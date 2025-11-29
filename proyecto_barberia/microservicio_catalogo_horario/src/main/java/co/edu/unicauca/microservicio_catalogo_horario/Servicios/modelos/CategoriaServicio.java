package co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categoria")
public class CategoriaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Integer id;

    @Column(name = "categoria_nombre", nullable = false)
    private String nombre;

    @Column(name = "categoria_descripcion", nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Servicio> servicios;
}
