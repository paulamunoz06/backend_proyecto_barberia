package co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    Integer id(Integer id);
    Optional<Estado> findByNombre(String nombre);
}