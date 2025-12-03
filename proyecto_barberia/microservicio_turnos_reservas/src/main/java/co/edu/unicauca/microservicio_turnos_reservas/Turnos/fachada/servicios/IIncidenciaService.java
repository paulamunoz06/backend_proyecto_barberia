package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.IncidenciaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.IncidenciaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TipoIncidenciaDTORespuesta;

import java.util.List;

public interface IIncidenciaService {
    List<IncidenciaDTORespuesta> findByBarberoId(String id);

    List<IncidenciaDTORespuesta> findByTurnoId(Integer id);

    List<IncidenciaDTORespuesta> findByReservaId(Integer id);

    IncidenciaDTORespuesta findById(Integer id);

    IncidenciaDTORespuesta save(IncidenciaDTOPeticion servicio);

    List<TipoIncidenciaDTORespuesta> listarTipoIncidencias();
}
