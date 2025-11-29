package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class User {
  @Id
  @Column(name = "usuario_id")
  private String username;

  @Column(name = "usuario_password", nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id"))
  private Set<Role> roles = new HashSet<>();

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
