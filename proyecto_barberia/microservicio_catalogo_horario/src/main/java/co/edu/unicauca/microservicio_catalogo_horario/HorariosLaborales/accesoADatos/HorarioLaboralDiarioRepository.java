package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HorarioLaboralDiarioRepository extends JpaRepository<Horario, LocalDate> {

}
