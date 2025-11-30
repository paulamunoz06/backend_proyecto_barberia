package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos.ClienteRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ServicioDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos.EventPublisher;
import co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos.NotificacionDTO;
import co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.REST.CatalogoServiceClient;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.EstadoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos.ReservaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.TurnoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private EventPublisher publicadorEventos;

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
    public List<TurnoDTORespuesta> findByBarberoAndFecha(String id, LocalDate fecha) {
        List<Turno> turnos = turnoRepository.findTurnosActivosByBarberoAndFecha(id,fecha);
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos para el barbero: " + id + " en la fecha" + fecha);
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
    public List<TurnoDTORespuesta> findByServicioIdActivos(Integer id) {
        List<Turno> turnos = turnoRepository.findByServicioIdAndEstadoActivo(id);
        if (turnos.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay turnos actualmente para el servicio: " + id);
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
        validarTurno(turnoDTO);

        Cliente cliente = repoCliente.findById(turnoDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + turnoDTO.getCliente()));
        Estado estado = repoEstado.findById(1).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado"));

        if (turnoDTO.getReserva() != null) {
            Reserva reserva = repoReserva.findById(turnoDTO.getReserva()).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + turnoDTO.getReserva()));
        }

        Turno turno = mapearAPersistencia(turnoDTO);
        turno.setCliente(cliente);
        turno.setEstado(estado);
        if (turnoDTO.getHoraInicio() == null) {
            turno.setHoraInicio(encontrarHora(turnoDTO.getBarberoId(),turnoDTO.getServicioId(),turnoDTO.getFechaInicio()));
            turno.setHoraFin(calcularHoraFin(turnoDTO.getHoraInicio(),turnoDTO.getServicioId()));

        }else {
            turno.setHoraInicio(turnoDTO.getHoraInicio());
            turno.setHoraFin(turnoDTO.getHoraFin());
        }

        Turno turnoGuardado = turnoRepository.save(turno);
        return mapearARespuesta(turnoGuardado);
    }

    @Override
    public TurnoDTORespuesta update(Integer id, TurnoDTORespuesta turnoDTO) {
        TurnoDTOPeticion turnoDTOValidacion = new TurnoDTOPeticion();
        turnoDTOValidacion.setBarberoId(turnoDTO.getBarberoId());
        turnoDTOValidacion.setServicioId(turnoDTO.getServicioId());
        turnoDTOValidacion.setFechaInicio(turnoDTO.getFechaInicio());
        turnoDTOValidacion.setHoraInicio(turnoDTO.getHoraInicio());
        turnoDTOValidacion.setHoraFin(turnoDTO.getHoraFin());

        validarTurno(turnoDTOValidacion);

        Turno turnoExistente = turnoRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + id));
        Cliente cliente = repoCliente.findById(turnoDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + turnoDTO.getCliente()));
        Estado estado = repoEstado.findById(turnoDTO.getEstado()).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID: " + turnoDTO.getEstado()));

        turnoExistente.setReserva(turnoExistente.getReserva());
        turnoExistente.setCliente(cliente);
        turnoExistente.setServicioId(turnoDTO.getServicioId());
        turnoExistente.setBarberoId(turnoDTO.getBarberoId());
        turnoExistente.setEstado(estado);
        turnoExistente.setDescripcion(turnoDTO.getDescripcion());
        turnoExistente.setFechaInicio(turnoDTO.getFechaInicio());
        if (turnoDTOValidacion.getHoraInicio() == null) {
            turnoExistente.setHoraInicio(encontrarHora(turnoDTO.getBarberoId(), turnoDTO.getServicioId(),turnoDTO.getFechaInicio()));
            turnoExistente.setHoraFin(calcularHoraFin(turnoExistente.getHoraInicio(), turnoDTO.getServicioId()));
        }else {
            turnoExistente.setHoraInicio(turnoDTOValidacion.getHoraInicio());
            turnoExistente.setHoraFin(turnoDTOValidacion.getHoraFin());
        }

        Turno turnoActualizado = turnoRepository.save(turnoExistente);
        return mapearARespuesta(turnoActualizado);
    }

    @Override
    public TurnoDTORespuesta updateEstado(Integer idTurno, Integer idEstado) {
        Turno turnoExistente = turnoRepository.findById(idTurno).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + idTurno));
        Estado estado = repoEstado.findById(idEstado).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID: " + idEstado));
        turnoExistente.setEstado(estado);
        Turno turnoActualizado = turnoRepository.save(turnoExistente);
        return mapearARespuesta(turnoActualizado);
    }

    @Override
    public void turnoClienteNoPresentado(Integer idTurno) {
        Turno turnoExistente = turnoRepository.findById(idTurno).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + idTurno));
        Cliente cliente = repoCliente.getReferenceById(turnoExistente.getCliente().getId());

        Estado estado = repoEstado.findById(4).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado con ID" ));
        turnoExistente.setEstado(estado);
        turnoExistente.setHoraFin(LocalTime.now());

        NotificacionDTO notificacion = new NotificacionDTO(cliente.getEmail(), "Querido cliente, su cita ha sido cancelada debido a que no se presentó en el horario asignado. Fecha y hora del turno: " + turnoExistente.getFechaInicio() + " - " + turnoExistente.getHoraFin() );
        publicadorEventos.enviarNotificacionCliente(notificacion);

        turnoRepository.save(turnoExistente);
    }

    @Override
    public LocalTime encontrarHora(String idBarbero, Integer idServicio, LocalDate fechaInicio) {
        ServicioDTORespuesta servicio = catalogoServiceClient.buscarServicio(idServicio);
        int duracionTotal = 10 + servicio.getDuracion() + servicio.getPreparacion();

        final LocalTime horaFinJornada = (fechaInicio.getDayOfWeek() == DayOfWeek.SATURDAY)
                ? LocalTime.of(13, 0)
                : LocalTime.of(20, 0);
        LocalTime horaBase = LocalTime.now();

        while (!horaBase.isAfter(horaFinJornada)) {

            LocalTime horaFin = horaBase.plusMinutes(duracionTotal);
            if (horaFin.isAfter(horaFinJornada)) {
                break;
            }

            boolean disponible = catalogoServiceClient.validarDuracionContinua(idBarbero, fechaInicio, horaBase, horaFin);
            if (disponible) {
                return horaBase;
            }

            horaBase = horaBase.plusMinutes(10);
        }
        throw new ReglaNegocioExcepcion("El barbero " + idBarbero + "no tiene disponibilidad el dia "+fechaInicio);
    }

    @Override
    public boolean delete(Integer id) {
        Turno turno = turnoRepository.findById(id).orElseThrow(() -> new EntidadNoExisteException("Turno no encontrado con ID: " + id));
        turnoRepository.delete(turno);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidadBarbero(String barberoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("Hora fin debe ser posterior a hora inicio");
        }

        List<Turno> turnosOcupados = turnoRepository.findTurnosActivosByBarberoAndFecha(barberoId, fecha);

        if (turnosOcupados.isEmpty()) {
            return true;
        }

        boolean haySolapamiento = turnosOcupados.stream()
                .anyMatch(turno -> seSolapan(
                        horaInicio, horaFin,
                        turno.getHoraInicio(), turno.getHoraFin()
                ));

        return !haySolapamiento;
    }

    public void eliminarTurnosActivosPorBarbero(String barberoId) {
        List<Turno> turnosActivos = turnoRepository.findByBarberoIdAndEstadoActivo(barberoId);
        if (turnosActivos.isEmpty()) {
            System.out.println("No hay turnos activos para eliminar para el barbero " + barberoId);
            return;
        }
        Estado estado = repoEstado.getReferenceById(3);
        turnosActivos.forEach(t -> t.setEstado(estado));
        System.out.println("Se eliminaron " + turnosActivos.size() + " turnos activos del barbero " + barberoId);
    }

    public void eliminarTurnosActivosPorServicio(Integer servicioId) {
        List<Turno> turnosActivos = turnoRepository.findByServicioIdAndEstadoActivo(servicioId);
        if (turnosActivos.isEmpty()) {
            System.out.println("No hay turnos activos para eliminar para el servicio " + servicioId);
            return;
        }
        Estado estado = repoEstado.getReferenceById(3);
        turnosActivos.forEach(t -> t.setEstado(estado));
        turnoRepository.saveAll(turnosActivos);
        System.out.println("Se eliminaron " + turnosActivos.size() + " turnos activos con el servicio " + servicioId);
    }

    private boolean seSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        if (inicio1 == null || fin1 == null || inicio2 == null || fin2 == null) {
            return false;
        }
        return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
    }

    public void validarTurno(TurnoDTOPeticion turnoDTO) {
        // Validar barbero
        if (!catalogoServiceClient.validarBarbero(turnoDTO.getBarberoId())) {
            throw new EntidadNoExisteException("Barbero no encontrado con ID: " + turnoDTO.getBarberoId());
        }

        //validar que el barbero trabaje ese dia
        LocalDateTime fecha = null;
        if (turnoDTO.getHoraInicio() != null) {
            LocalTime hora = LocalTime.of(8, 0);
            fecha = turnoDTO.getFechaInicio().atTime(hora);
        }else{
            fecha = LocalDateTime.now();
        }
        if (!catalogoServiceClient.validarTrabajoDiaBarbero(turnoDTO.getBarberoId(), fecha)) {
            throw new EntidadNoExisteException("El barbero: " + turnoDTO.getBarberoId() + " no trabaja el dia " + turnoDTO.getFechaInicio());
        }

        // Validar servicio
        if (!catalogoServiceClient.validarServicio(turnoDTO.getServicioId())) {
            throw new EntidadNoExisteException("Servicio no encontrado con ID: " + turnoDTO.getServicioId());
        }

        // Validar fechas
        if(turnoDTO.getFechaInicio() != null){
            validarFechasTurno(turnoDTO.getFechaInicio(), turnoDTO.getHoraInicio());
        }
    }

    private void validarFechasTurno(LocalDate fechaInicio, LocalTime horaInicio) {
        LocalDate hoy = LocalDate.now();

        if (fechaInicio.isBefore(hoy)) {
            throw new ReglaNegocioExcepcion("No se puede crear un turno para fechas pasadas");
        }
        if (fechaInicio.isEqual(hoy) && horaInicio != null) {
            if (horaInicio.isBefore(LocalTime.now())) {
                throw new ReglaNegocioExcepcion("No se puede crear un turno para horas pasadas");
            }
        }
    }

    public LocalTime calcularHoraFin(LocalTime horaInicio, Integer servicioId) {
        ServicioDTORespuesta servicio = catalogoServiceClient.buscarServicio(servicioId);
        int duracionTotal = 10 + servicio.getDuracion() + servicio.getPreparacion();
        return horaInicio.plusMinutes(duracionTotal);
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
        dto.setHoraInicio(t.getHoraInicio());
        dto.setHoraFin(t.getHoraFin());
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
        t.setDescripcion(dto.getDescripcion());
        t.setFechaInicio(dto.getFechaInicio());
        t.setHoraInicio(dto.getHoraInicio());
        t.setHoraFin(dto.getHoraFin());
        return t;
    }
}
