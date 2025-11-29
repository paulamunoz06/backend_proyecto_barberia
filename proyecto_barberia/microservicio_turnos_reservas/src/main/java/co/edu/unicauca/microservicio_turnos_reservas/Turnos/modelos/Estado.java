package co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_id")
    private Integer id;

    @Column(name = "estado_nombre", length = 50, nullable = false)
    private String nombre;
}