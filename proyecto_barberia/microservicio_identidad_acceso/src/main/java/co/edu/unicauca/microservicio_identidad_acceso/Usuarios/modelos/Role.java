package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rol_id")
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "rol_nombre", nullable = false, length = 50)
  private ERole name;

}