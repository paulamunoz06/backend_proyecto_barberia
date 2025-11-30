package co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    @Query("SELECT t FROM Turno t WHERE t.barberoId = :barberoId")
    List<Turno> findByBarberoId(@Param("barberoId") String barberoId);

    @Query("SELECT t FROM Turno t WHERE t.reserva.id = :reservaId")
    List<Turno> findByReservaId(@Param("reservaId") Integer reservaId);

    @Query("SELECT t FROM Turno t WHERE t.barberoId = :barberoId AND t.estado.id IN (1, 2)")
    List<Turno> findByBarberoIdAndEstadoActivo(@Param("barberoId") String barberoId);

    @Query("SELECT t FROM Turno t WHERE t.barberoId = :barberoId AND t.fechaInicio = :fecha AND t.estado.id IN (1, 2)")
    List<Turno> findTurnosActivosByBarberoAndFecha(
            @Param("barberoId") String barberoId,
            @Param("fecha") LocalDate fecha
    );

    Optional<Turno> findById(Integer id);
}