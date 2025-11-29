package co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    @Query("SELECT s FROM Servicio s WHERE s.categoria.id = :categoriaId")
    List<Servicio> findByCategoriaId(@Param("categoriaId") Integer categoriaId);

    @Query("SELECT s FROM Servicio s JOIN s.barberos b WHERE b.id = :barberoId")
    List<Servicio> findByBarberoId(@Param("barberoId") String barberoId);
}
