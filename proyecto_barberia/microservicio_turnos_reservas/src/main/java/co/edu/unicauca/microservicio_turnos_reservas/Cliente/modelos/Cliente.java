package co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "cliente_id", length = 20)
    private String id;

    @Column(name = "cliente_nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "cliente_email", length = 50, nullable = false)
    private String email;

    @Column(name = "cliente_telefono", nullable = false)
    private Long telefono;

    @Column(name = "cliente_estado", length = 20, nullable = false)
    private String estado;
}