package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.IncidenciaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.TipoIncidenciaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.TurnoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.IncidenciaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.IncidenciaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Incidencia;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.TipoIncidencia;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidenciaServiceImpl implements IIncidenciaService{
    @Autowired
    private IncidenciaRepository repo;

    @Autowired
    private TurnoRepository repoTurno;

    @Autowired
    private TipoIncidenciaRepository repoTipo;

    @Override
    public List<IncidenciaDTORespuesta> findByBarberoId(String id) {
        List<Incidencia> incidencias = repo.findByBarberoId(id);
        if (incidencias.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay incidencias para el barbero: " + id);
        }
        return incidencias.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());

    }

    @Override
    public List<IncidenciaDTORespuesta> findByTurnoId(Integer id) {
        List<Incidencia> incidencias = repo.findByTurnoId(id);
        if (incidencias.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay incidencias para el turno: " + id);
        }
        return incidencias.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidenciaDTORespuesta> findByReservaId(Integer id) {
        List<Incidencia> incidencias = repo.findByReservaId(id);
        if (incidencias.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay incidencias para la reserva: " + id);
        }
        return incidencias.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenciaDTORespuesta findById(Integer id) {
            Incidencia incidencia = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("Incidencia no encontrada con ID: " + id));
            return mapearARespuesta(incidencia);
    }

    @Override
    public IncidenciaDTORespuesta save(IncidenciaDTOPeticion incidenciaDTO) {
        Turno turno = repoTurno.findById(incidenciaDTO.getTurno()).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + incidenciaDTO.getTurno()));
        TipoIncidencia tipoIncidencia = repoTipo.findById(incidenciaDTO.getTipoIncidencia()).orElseThrow(() -> new EntidadNoExisteException("Tipo de incidencia no encontrado con ID: " + incidenciaDTO.getTipoIncidencia()));

        Incidencia incidencia = mapearAPersistencia(incidenciaDTO);
        incidencia.setTurno(turno);
        incidencia.setTipoIncidencia(tipoIncidencia);

        Incidencia incidenciaGuardada = repo.save(incidencia);

        return mapearARespuesta(incidenciaGuardada);
    }

    public IncidenciaDTORespuesta mapearARespuesta(Incidencia i) {
        IncidenciaDTORespuesta dto = new IncidenciaDTORespuesta();
        dto.setId(i.getId());
        dto.setTurno(i.getTurno().getId());
        dto.setTipoIncidencia(i.getTipoIncidencia().getId());
        dto.setDescripcion(i.getDescripcion());
        return dto;
    }

    public Incidencia mapearAPersistencia(IncidenciaDTOPeticion dto) {
        Incidencia i = new Incidencia();
        i.setTurno(repoTurno.getReferenceById(dto.getTurno()));
        i.setTipoIncidencia(repoTipo.getReferenceById(dto.getTipoIncidencia()));
        i.setDescripcion(dto.getDescripcion());
        return i;
    }

}
