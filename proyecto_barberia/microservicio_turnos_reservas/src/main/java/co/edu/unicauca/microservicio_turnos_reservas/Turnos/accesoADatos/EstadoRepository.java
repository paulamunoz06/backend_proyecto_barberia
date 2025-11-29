package co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}