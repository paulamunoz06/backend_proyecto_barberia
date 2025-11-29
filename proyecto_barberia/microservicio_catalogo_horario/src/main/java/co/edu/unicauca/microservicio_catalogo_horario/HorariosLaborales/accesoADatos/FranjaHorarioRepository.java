package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FranjaHorarioRepository extends JpaRepository<Franja, Integer> {
    List<Franja> findByHorario_Id(LocalDate horarioId);

    List<Franja> findByBarberos_Id(String barberoId);

    boolean existsByHorario_IdAndHoraInicioAndHoraFin(
            LocalDate horarioId,
            LocalTime horaInicio,
            LocalTime horaFin
    );

    @Query("""
        SELECT COUNT(f) > 0
        FROM Franja f
        WHERE f.horario.id = :horarioId
        AND (
                f.horaInicio < :hfin
            AND f.horaFin    > :hinicio
        )
    """)
    boolean existsOverlapping(
            @Param("horarioId") LocalDate horarioId,
            @Param("hinicio") LocalTime hinicio,
            @Param("hfin") LocalTime hfin
    );

    @Query("SELECT f FROM Franja f JOIN f.barberos b WHERE b.id = :barberoId ORDER BY f.horario.id, f.horaInicio")
    List<Franja> findAllByBarbero(@Param("barberoId") String barberoId);

    @Query("""
        SELECT f FROM Franja f
        JOIN f.barberos b
        WHERE b.id = :barberoId
          AND (f.horario.id > :fecha OR (f.horario.id = :fecha AND f.horaInicio >= :hora))
        ORDER BY f.horario.id, f.horaInicio
    """)
    List<Franja> findFranjasBarberoDesde(
            @Param("barberoId") String barberoId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END
        FROM Franja f
        JOIN f.barberos b
        WHERE b.id = :barberoId
          AND f.horario.id = :horarioId
    """)
    boolean existsByBarberoAndHorario(
            @Param("barberoId") String barberoId,
            @Param("horarioId") LocalDate horarioId
    );
}
