package co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, String> {

}
