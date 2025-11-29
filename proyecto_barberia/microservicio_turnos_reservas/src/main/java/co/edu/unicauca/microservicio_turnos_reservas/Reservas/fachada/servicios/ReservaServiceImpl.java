package co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos.ClienteRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_turnos_reservas.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos.ReservaRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.TurnoServiceImpl;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements IReservaService{

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private TurnoServiceImpl serviceTurno;

    @Autowired
    private ReservaRepository repo;

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
        Reserva horario = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("No existe una reserva con id: " + id));
        return mapearARespuesta(horario);
    }

    @Override
    public List<ReservaDTORespuesta> findByClienteId(String id) {
        return List.of();
    }

    @Override
    public ReservaDTORespuesta save(ReservaDTOPeticion reservaDTO) {
        validarFechaReserva(reservaDTO.getFechaReserva());

        Cliente cliente = repoCliente.findById(reservaDTO.getCliente()).orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + reservaDTO.getCliente()));

        Reserva reserva = mapearAPersistencia(reservaDTO);
        reserva.setCliente(cliente);

        Reserva reservaGuardada = repo.save(reserva);

        return mapearARespuesta(reservaGuardada);
    }

    @Override
    public ReservaDTORespuesta update(Integer id, ReservaDTOPeticion reservaDTO) {
        Reserva reservaExistente = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + id));

        validarFechaReserva(reservaDTO.getFechaReserva());

        Cliente cliente = repoCliente.findById(reservaDTO.getCliente())
                .orElseThrow(() -> new EntidadNoExisteException("Cliente no encontrado con ID: " + reservaDTO.getCliente()));

        reservaExistente.setCliente(cliente);
        reservaExistente.setFechaReserva(reservaDTO.getFechaReserva());

        for (Turno turno : reservaExistente.getTurnos()) {
            serviceTurno.delete(turno.getId());
        }
        reservaExistente.getTurnos().clear();

        for (TurnoDTOPeticion turnoDTO : reservaDTO.getTurnos()) {
            turnoDTO.setReserva(id);
            TurnoDTORespuesta nuevoTurno = serviceTurno.save(turnoDTO);
            Turno turnoEntity = serviceTurno.mapearAPersistenciaRespuesta(nuevoTurno);
            turnoEntity.setReserva(reservaExistente);
            reservaExistente.getTurnos().add(turnoEntity);
        }

        Reserva reservaActualizada = repo.save(reservaExistente);
        return mapearARespuesta(reservaActualizada);
    }

    @Override
    public boolean delete(Integer id) {
        Reserva reserva = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("Reserva no encontrada con ID: " + id));
        repo.delete(reserva);
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
                .toList();

        dto.setTurnos(turnosDTO);
        return dto;
    }

    private Reserva mapearAPersistencia(ReservaDTOPeticion dto) {
        Reserva r = new Reserva();
        r.setCliente(repoCliente.getReferenceById(dto.getCliente()));
        r.setFechaReserva(dto.getFechaReserva());

        List<Turno> turnos = new ArrayList<>();
        for (TurnoDTOPeticion turnoDTO : dto.getTurnos()) {
            Turno turno = this.serviceTurno.mapearAPersistencia(turnoDTO);
            turno.setReserva(r);
            turnos.add(turno);
        }

        r.setTurnos(turnos);
        return r;
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
