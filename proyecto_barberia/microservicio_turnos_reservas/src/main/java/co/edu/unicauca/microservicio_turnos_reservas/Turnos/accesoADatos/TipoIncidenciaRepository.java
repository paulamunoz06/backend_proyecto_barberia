package co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Integer> {
}