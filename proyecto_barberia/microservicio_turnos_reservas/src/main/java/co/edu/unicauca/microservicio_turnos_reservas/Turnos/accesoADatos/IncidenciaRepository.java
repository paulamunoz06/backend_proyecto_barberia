package co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    @Query("SELECT i FROM Incidencia i WHERE i.turno.barberoId = :barberoId")
    List<Incidencia> findByBarberoId(@Param("barberoId") String barberoId);

    @Query("SELECT i FROM Incidencia i WHERE i.turno.id = :turnoId")
    List<Incidencia> findByTurnoId(@Param("turnoId") Integer turnoId);

    @Query("SELECT i FROM Incidencia i WHERE i.turno.reserva.id = :reservaId")
    List<Incidencia> findByReservaId(@Param("reservaId") Integer reservaId);

    Optional<Incidencia> findById(Integer id);
}