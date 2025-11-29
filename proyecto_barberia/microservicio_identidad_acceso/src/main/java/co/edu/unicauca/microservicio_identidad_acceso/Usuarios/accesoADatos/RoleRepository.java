package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.ERole;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
