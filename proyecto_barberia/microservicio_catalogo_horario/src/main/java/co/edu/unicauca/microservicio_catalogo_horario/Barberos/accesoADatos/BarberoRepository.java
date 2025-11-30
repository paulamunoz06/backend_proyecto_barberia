package co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BarberoRepository extends JpaRepository<Barbero, String> {
    List<Barbero> findByEstado(EstadoUsuario estado);
    @Query("""
       SELECT b
       FROM Barbero b
       JOIN b.serviciosEspecializados s
       WHERE s.id = :servicioId
       AND b.estado = 'ACTIVO'
       """)
    List<Barbero> findBarberosActivosByServicioId(@Param("servicioId") Integer servicioId);
}