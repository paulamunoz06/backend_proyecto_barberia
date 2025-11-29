package co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarberoRepository extends JpaRepository<Barbero, String> {
    List<Barbero> findByEstado(EstadoUsuario estado);
    List<Barbero> findByServiciosEspecializados_Id(Integer servicioId);
}