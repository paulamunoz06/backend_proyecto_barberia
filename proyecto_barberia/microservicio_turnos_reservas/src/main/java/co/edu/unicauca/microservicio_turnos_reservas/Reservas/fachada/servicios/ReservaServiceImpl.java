package co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos.ClienteRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Email.NotificacionesCliente;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos.ReservaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.EstadoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.TurnoServiceImpl;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements IReservaService{

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private EstadoRepository repoEstado;

    @Autowired
    private TurnoServiceImpl serviceTurno;

    @Autowired
    private ReservaRepository repo;

    @Autowired
    private NotificacionesCliente notificacionService;

    @Override
    public List<ReservaDTORespuesta> findAll() {
        List<Reserva> reservas = repo.findAll();
        if (reservas.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay reservas");
        }
        return reservas.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaDTORespuesta findById(Integer id) {
        Reserva reserva = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("No existe una reserva con id: " + id));
        return mapearARespuesta(reserva);
    }

    @Override
    public List<ReservaDTORespuesta> findByClienteId(String id) {
        List<Reserva> reservas = repo.findByClienteId(id);
        if (reservas.isEmpty()) {
            throw new EntidadNoExisteException("No hay reservas para el cliente con ID: " + id);
        }
        return reservas.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaDTORespuesta save(ReservaDTOPeticion reservaDTO) {
        validarFechaReserva(reservaDTO.getFechaReserva());

        Cliente cliente = repoCliente.findById(reservaDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + reservaDTO.getCliente()));
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setCliente(cliente);

        if (reservaDTO.getTurnos() != null && !reservaDTO.getTurnos().isEmpty()) {
            for (TurnoDTOPeticion turnoDTO : reservaDTO.getTurnos()) {
                serviceTurno.validarTurno(turnoDTO);

                Turno turno = new Turno();
                turno.setCliente(cliente);
                turno.setServicioId(turnoDTO.getServicioId());
                turno.setBarberoId(turnoDTO.getBarberoId());
                turno.setFechaInicio(reservaDTO.getFechaReserva());
                turno.setHoraInicio(turnoDTO.getHoraInicio());
                if(turnoDTO.getHoraFin() != null) {
                    turno.setHoraFin(turnoDTO.getHoraFin());
                }
                else{
                    turno.setHoraFin(serviceTurno.calcularHoraFin(turno.getHoraInicio(),turno.getServicioId()));
                }

                Estado estado = repoEstado.findById(1).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado"));
                turno.setEstado(estado);

                reserva.agregarTurno(turno);
            }
        } else {
            throw new ReglaNegocioExcepcion("La reserva debe tener al menos un turno");
        }

        Reserva reservaGuardada = repo.save(reserva);
        return mapearARespuesta(reservaGuardada);
    }

    @Override
    @Transactional
    public ReservaDTORespuesta update(Integer id, ReservaDTORespuesta reservaDTO) {
        Reserva reservaExistente = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + id));
        if (reservaExistente.getFechaReserva().isBefore(LocalDate.now())) {
            throw new ReglaNegocioExcepcion("No se puede editar una reserva pasada");
        }

        validarFechaReserva(reservaDTO.getFechaReserva());

        reservaExistente.setCliente(reservaExistente.getCliente());
        reservaExistente.setFechaReserva(reservaDTO.getFechaReserva());
        reservaExistente.getTurnos().clear();

        if (reservaDTO.getTurnos() != null && !reservaDTO.getTurnos().isEmpty()) {
            for (TurnoDTORespuesta turnoDTO : reservaDTO.getTurnos()) {

                TurnoDTOPeticion turnoValidar = new TurnoDTOPeticion();
                turnoValidar.setBarberoId(turnoDTO.getBarberoId());
                turnoValidar.setServicioId(turnoDTO.getServicioId());
                turnoValidar.setFechaInicio(reservaDTO.getFechaReserva());
                turnoValidar.setHoraInicio(turnoDTO.getHoraInicio());
                serviceTurno.validarTurno(turnoValidar);

                Turno turno = new Turno();
                turno.setCliente(reservaExistente.getCliente());
                turno.setServicioId(turnoDTO.getServicioId());
                turno.setBarberoId(turnoDTO.getBarberoId());
                turno.setFechaInicio(turnoDTO.getFechaInicio());
                turno.setHoraInicio(turnoDTO.getHoraInicio());
                if(turnoDTO.getHoraFin() != null) {
                    turno.setHoraFin(turnoDTO.getHoraFin());
                }
                else{
                    turno.setHoraFin(serviceTurno.calcularHoraFin(turno.getHoraInicio(),turno.getServicioId()));
                }


                Estado estado = repoEstado.findById(1).orElseThrow(() -> new EntidadNoExisteException("Estado no encontrado"));
                turno.setEstado(estado);

                reservaExistente.agregarTurno(turno);
            }
        } else {
            throw new ReglaNegocioExcepcion("La reserva debe tener al menos un turno");
        }

        Reserva reservaActualizada = repo.save(reservaExistente);
        return mapearARespuesta(reservaActualizada);
    }

    @Override
    @Transactional
    public boolean cancelar(Integer id) {
        Reserva reserva = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + id));

        if (reserva.getFechaReserva().isBefore(LocalDate.now())) {
            throw new ReglaNegocioExcepcion("No se puede cancelar una reserva pasada");
        }

        Estado estadoCancelado = repoEstado.findById(3).orElseThrow(() -> new EntidadNoExisteException("Estado cancelado no encontrado"));

        for (Turno turno : reserva.getTurnos()) {
            turno.setEstado(estadoCancelado);
        }

        repo.save(reserva);
        Cliente cliente = repoCliente.getReferenceById(reserva.getCliente().getId());
        notificacionService.notificarEliminacionReserva(cliente.getEmail(), reserva.getFechaReserva().toString());
        return true;
    }

    public ReservaDTORespuesta mapearARespuesta(Reserva r) {
        ReservaDTORespuesta dto = new ReservaDTORespuesta();
        dto.setId(r.getId());
        dto.setCliente(r.getCliente().getId());
        dto.setFechaReserva(r.getFechaReserva());

        List<TurnoDTORespuesta> turnosDTO = r.getTurnos()
                .stream()
                .map(this.serviceTurno::mapearARespuesta)
                .collect(Collectors.toList());

        dto.setTurnos(turnosDTO);
        return dto;
    }

    private void validarFechaReserva(LocalDate fechaReserva) {
        LocalDate hoy = LocalDate.now();

        LocalDate fechaMinima = hoy.plusDays(1);
        if (fechaReserva.isBefore(fechaMinima)) {
             throw new ReglaNegocioExcepcion("Solo se pueden crear reservas con al menos 1 día de anticipación");
        }

        if (fechaReserva.isBefore(hoy)) {
            throw new ReglaNegocioExcepcion("No se puede crear una reserva para fechas pasadas");
        }
    }
}