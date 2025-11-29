package co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servicio_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaServicio categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;

    @Column(name = "servicio_descripcion", nullable = false)
    private String descripcion;

    @Column(name = "servicio_duracion", nullable = false)
    private Integer duracion;

    @Column(name = "servicio_precio", nullable = false)
    private Float precio;

    @Column(name = "servicio_preparacion", nullable = false)
    private Integer preparacion;

    @Column(name = "servicio_nombre", nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "serviciosEspecializados", fetch = FetchType.LAZY)
    private List<Barbero> barberos;

    public Servicio(Integer id) {
        this.id = id;
    }
}
