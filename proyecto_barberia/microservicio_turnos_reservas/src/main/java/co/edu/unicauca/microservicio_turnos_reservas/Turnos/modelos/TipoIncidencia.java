package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tipo_incidencia")
public class TipoIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoincidencia_id")
    private Integer id;

    @Column(name = "tipoincidencia_nombre", length = 50, nullable = false)
    private String nombre;
}