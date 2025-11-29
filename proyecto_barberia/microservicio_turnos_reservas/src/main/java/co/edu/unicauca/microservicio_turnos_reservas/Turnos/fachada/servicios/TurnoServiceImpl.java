package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos.ClienteRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.EstadoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos.ReservaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.TurnoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticionBarbero;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoServiceImpl implements ITurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ReservaRepository repoReserva;

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private EstadoRepository repoEstado;

    @Autowired
    private CatalogoServiceClient catalogoServiceClient;

    @Autowired
    private TurnoDinamico turnoDinamico;

    @Override
    public List<TurnoDTORespuesta> findAll() {
        List<Turno> turnos = turnoRepository.findAll();
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos actualmente.");
        }
        return turnos.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoDTORespuesta> findByBarberoId(String id) {
        List<Turno> turnos = turnoRepository.findByBarberoId(id);
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos actualmente para el barbero: " + id);
        }
        return turnos.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoDTORespuesta> findByReservaId(Integer reservaId) {
        List<Turno> turnos = turnoRepository.findByReservaId(reservaId);
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos actualmente para la reserva: " + reservaId);
        }
        return turnos.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoDTORespuesta> findByBarberoIdActivos(String id) {
        List<Turno> turnos = turnoRepository.findByBarberoIdAndEstadoActivo(id);
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos actualmente para el barbero: " + id);
        }
        return turnos.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoDTORespuesta findById(Integer id) {
        Turno turno = turnoRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + id));
        return mapearARespuesta(turno);
    }

    @Override
    public TurnoDTORespuesta save(TurnoDTOPeticion turnoDTO) {
        validarFechaReserva(turnoDTO.getFechaInicio());

        Cliente cliente = repoCliente.findById(turnoDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + turnoDTO.getCliente()));
        Estado estado = repoEstado.findById(turnoDTO.getEstado()).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID: " + turnoDTO.getEstado()));

        if (!catalogoServiceClient.validarBarbero(turnoDTO.getBarberoId())) {
            throw new EntidadNoExisteException("Barbero no encontrado con ID: " + turnoDTO.getBarberoId());
        }
        if (!catalogoServiceClient.validarServicio(turnoDTO.getServicioId())) {
            throw new EntidadNoExisteException("Servicio no encontrado con ID: " + turnoDTO.getServicioId());
        }

        if (turnoDTO.getReserva() != null) {
            Reserva reserva = repoReserva.findById(turnoDTO.getReserva()).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + turnoDTO.getReserva()));
        }

        Turno turno = mapearAPersistencia(turnoDTO);
        turno.setCliente(cliente);
        turno.setEstado(estado);

        Turno turnoGuardado = turnoRepository.save(turno);

        return mapearARespuesta(turnoGuardado);
    }

    @Override
    public TurnoDTORespuesta saveBarbero(TurnoDTOPeticionBarbero turnoDTO) {
        Cliente cliente = repoCliente.findById(turnoDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + turnoDTO.getCliente()));
        Estado estado = repoEstado.findById(turnoDTO.getEstado()).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID: " + turnoDTO.getEstado()));

        if (!catalogoServiceClient.validarBarbero(turnoDTO.getBarberoId())) {
            throw new EntidadNoExisteException("Barbero no encontrado con ID: " + turnoDTO.getBarberoId());
        }
        if (!catalogoServiceClient.validarServicio(turnoDTO.getServicioId())) {
            throw new EntidadNoExisteException("Servicio no encontrado con ID: " + turnoDTO.getServicioId());
        }
        Turno turno = mapearAPersistenciaBarbero(turnoDTO);
        turno.setCliente(cliente);
        turno.setEstado(estado);
        turno.setHoraInicio(encontrarHora(mapearARespuesta(turno)));

        Turno turnoGuardado = turnoRepository.save(turno);

        return mapearARespuesta(turnoGuardado);
    }

    @Override
    public TurnoDTORespuesta update(Integer id, TurnoDTORespuesta turnoDTO) {
        validarFechaReserva(turnoDTO.getFechaInicio());
        Turno turnoExistente = turnoRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + id));
        Cliente cliente = repoCliente.findById(turnoDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + turnoDTO.getCliente()));
        Estado estado = repoEstado.findById(turnoDTO.getEstado()).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID: " + turnoDTO.getEstado()));

        if (!catalogoServiceClient.validarBarbero(turnoDTO.getBarberoId())) {
            throw new EntidadNoExisteException("Barbero no encontrado con ID: " + turnoDTO.getBarberoId());
        }
        if (!catalogoServiceClient.validarServicio(turnoDTO.getServicioId())) {
            throw new EntidadNoExisteException("Servicio no encontrado con ID: " + turnoDTO.getServicioId());
        }

        turnoExistente.setReserva(turnoExistente.getReserva());
        turnoExistente.setCliente(cliente);
        turnoExistente.setServicioId(turnoDTO.getServicioId());
        turnoExistente.setBarberoId(turnoDTO.getBarberoId());
        turnoExistente.setEstado(estado);
        turnoExistente.setDescripcion(turnoDTO.getDescripcion());
        turnoExistente.setFechaInicio(turnoDTO.getFechaInicio());
        turnoExistente.setHoraInicio(encontrarHora(turnoDTO));

        Turno turnoActualizado = turnoRepository.save(turnoExistente);
        return mapearARespuesta(turnoActualizado);

    }

    @Override
    public LocalTime encontrarHora(TurnoDTORespuesta turno) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        Turno turno = turnoRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + id));
        turnoRepository.delete(turno);
        return true;
    }

    public TurnoDTORespuesta mapearARespuesta(Turno t) {
        TurnoDTORespuesta dto = new TurnoDTORespuesta();
        dto.setId(t.getId());
        dto.setReserva(t.getReserva() != null ? t.getReserva().getId() : null);
        dto.setCliente(t.getCliente().getId());
        dto.setServicioId(t.getServicioId());
        dto.setBarberoId(t.getBarberoId());
        dto.setEstado(t.getEstado().getId());
        dto.setDescripcion(t.getDescripcion());
        dto.setFechaInicio(t.getFechaInicio());
        dto.setHoraFin(t.getHoraFin());
        dto.setHoraInicio(t.getHoraInicio());
        return dto;
    }

    public Turno mapearAPersistencia(TurnoDTOPeticion dto) {
        Turno t = new Turno();

        if (dto.getReserva() != null) {
            t.setReserva(repoReserva.getReferenceById(dto.getReserva()));
        }

        t.setCliente(repoCliente.getReferenceById(dto.getCliente()));
        t.setServicioId(dto.getServicioId());
        t.setBarberoId(dto.getBarberoId());
        t.setEstado(repoEstado.getReferenceById(dto.getEstado()));
        t.setDescripcion(dto.getDescripcion());
        t.setFechaInicio(dto.getFechaInicio());
        t.setHoraFin(dto.getHoraFin());
        t.setHoraInicio(dto.getHoraInicio());
        return t;
    }

    public Turno mapearAPersistenciaBarbero(TurnoDTOPeticionBarbero dto) {
        Turno t = new Turno();

        t.setCliente(repoCliente.getReferenceById(dto.getCliente()));
        t.setServicioId(dto.getServicioId());
        t.setBarberoId(dto.getBarberoId());
        t.setEstado(repoEstado.getReferenceById(dto.getEstado()));
        t.setDescripcion(dto.getDescripcion());
        t.setFechaInicio(LocalDate.now());
        t.setHoraFin(null);
        t.setHoraInicio(null);
        return t;
    }

    public Turno mapearAPersistenciaRespuesta(TurnoDTORespuesta dto) {
        Turno t = new Turno();
        t.setId(dto.getId());

        if (dto.getReserva() != null) {
            t.setReserva(repoReserva.getReferenceById(dto.getReserva()));
        }

        t.setCliente(repoCliente.getReferenceById(dto.getCliente()));
        t.setServicioId(dto.getServicioId());
        t.setBarberoId(dto.getBarberoId());
        t.setEstado(repoEstado.getReferenceById(dto.getEstado()));
        t.setDescripcion(dto.getDescripcion());
        t.setFechaInicio(dto.getFechaInicio());
        t.setHoraInicio(dto.getHoraInicio());
        t.setHoraFin(dto.getHoraFin());
        return t;
    }

    private void validarFechaReserva(LocalDate fechaReserva) {
        LocalDate hoy = LocalDate.now();
        if (fechaReserva.isBefore(hoy) || fechaReserva.isEqual(hoy)) {
            throw new ReglaNegocioExcepcion("No se puede editar una reserva para hoy o fechas pasadas");
        }
        LocalDate fechaMinima = hoy.plusDays(1);
        if (fechaReserva.isBefore(fechaMinima)) {
            throw new ReglaNegocioExcepcion("Solo se pueden editar reservas con al menos 1 día de anticipación");
        }
    }
}
