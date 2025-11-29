package co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.CategoriaServicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaServicioRepository extends JpaRepository<CategoriaServicio, Integer> {

}
