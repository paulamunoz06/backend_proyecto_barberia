package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}