package co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OcupacionRepository extends JpaRepository<Ocupacion, Integer> {
    Optional<Ocupacion> findByNombre(String nombre);
}
