package co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcupacionRepository extends JpaRepository<Ocupacion, Integer> {

}
