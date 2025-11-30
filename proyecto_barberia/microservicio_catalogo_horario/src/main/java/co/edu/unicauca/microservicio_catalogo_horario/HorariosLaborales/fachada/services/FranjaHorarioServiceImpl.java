package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos.BarberoRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services.BarberoServiceImpl;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos.FranjaHorarioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos.HorarioLaboralDiarioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class FranjaHorarioServiceImpl implements IFranjaHorarioService {
    @Autowired
    private FranjaHorarioRepository repoFranja;

    @Autowired
    private HorarioLaboralDiarioRepository repoHorario;

    @Autowired
    private BarberoRepository repoBarbero;

    @Autowired
    private BarberoServiceImpl barberoService;

    @Autowired
    private TurnoServiceClient turnoServiceClient;

    @Override
    public List<FranjaHorarioDTORespuesta> findHorario(LocalDate horarioId) {
        if (!repoHorario.existsById(horarioId)) {
            throw new ReglaNegocioExcepcion("No existe el horario con fecha: " + horarioId);
        }

        List<Franja> franjas = repoFranja.findByHorario_Id(horarioId);

        return franjas
                .stream()
                .map(this::mapearARespuesta)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FranjaHorarioDTORespuesta> findBarbero(String barberoId) {
        List<Franja> franjas = repoFranja.findAllByBarbero(barberoId);

        if (franjas.isEmpty()) {
            throw new ReglaNegocioExcepcion("El barbero no tiene franjas asignadas");
        }

        return franjas.stream()
                .map(this::mapearARespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FranjaHorarioDTORespuesta> findBarberoDesdeHora(String barberoId, LocalDate fecha, LocalTime hora) {
        if (!repoBarbero.existsById(barberoId)) {
            throw new EntidadNoExisteException("El barbero con ID " + barberoId + " no existe");
        }

        if(!repoFranja.existsByBarberoAndHorario(barberoId,fecha)){
            return null;
        }

        List<Franja> franjas = repoFranja.findFranjasBarberoDesde(barberoId, fecha, hora);

        if (franjas.isEmpty()) {
            return null;
        }

        return franjas.stream()
                .map(this::mapearARespuesta)
                .toList();
    }

    @Override
    public Boolean verificarBarberoDesdeHora(String barberoId, LocalDate fecha, LocalTime hora) {
        if(findBarberoDesdeHora(barberoId,fecha,hora) == null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneDuracionContinua(String barberoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        boolean disponibleEnFranjas = verificarDisponibilidadEnFranjas(barberoId, fecha, horaInicio, horaFin);

        if (!disponibleEnFranjas) {
            return false;
        }

        boolean sinConflictos = turnoServiceClient.verificarDisponibilidadBarbero(barberoId, fecha, horaInicio, horaFin);

        return sinConflictos;
    }

    @Override
    public FranjaHorarioDTORespuesta findById(Integer id) {
        Franja franja = repoFranja.findById(id).orElseThrow(() -> new ReglaNegocioExcepcion("No existe la franja con ID: " + id));
        return mapearARespuesta(franja);
    }

    @Override
    @Transactional
    public FranjaHorarioDTORespuesta save(FranjaHorarioDTOPeticion dto) {
        Horario horario = repoHorario.findById(dto.getHorarioId()).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el horario laboral diario con fecha: " + dto.getHorarioId()));

        validarFranja(dto.getHorarioId(), dto.getHoraInicio(), dto.getHoraFin(), null);

        Franja entidad = mapearAPersistencia(dto);
        Franja guardada = repoFranja.save(entidad);

        return mapearARespuesta(guardada);
    }

    @Override
    @Transactional
    public FranjaHorarioDTORespuesta update(Integer id, FranjaHorarioDTORespuesta dto) {
        if (!id.equals(dto.getId())) {
            throw new RuntimeException("El ID del path no coincide con el ID del cliente");
        }
        Franja existente = repoFranja.findById(id).orElseThrow(() -> new ReglaNegocioExcepcion("No existe la franja con ID " + id));

        if (!existente.getHoraInicio().equals(dto.getHoraInicio()) || !existente.getHoraFin().equals(dto.getHoraFin())) {
            throw new ReglaNegocioExcepcion("No se permite modificar la hora de inicio ni la hora fin de una franja existente");
        }

        if (!existente.getHorario().getId().equals(dto.getHorarioId())) {
            throw new ReglaNegocioExcepcion("No se permite mover la franja a otro horario laboral");
        }

        if (!existente.getId().equals(dto.getId())) {
            throw new ReglaNegocioExcepcion("No se modificar el Id de una franja");
        }

        List<Barbero> nuevosBarberos = new ArrayList<>();
        if (dto.getBarberos() != null) {
            Set<String> idsUnicos = new HashSet<>(dto.getBarberos());

            if (idsUnicos.size() > 5) {
                throw new ReglaNegocioExcepcion("No se pueden asignar más de 5 barberos a una franja. Número de puestos disponible: 5");
            }

            for (String idBarbero : idsUnicos) {
                Barbero b = repoBarbero.findById(idBarbero).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el barbero con ID " + idBarbero));
                nuevosBarberos.add(b);
            }
        }

        existente.setBarberos(nuevosBarberos);
        Franja actualizada = repoFranja.save(existente);
        return mapearARespuesta(actualizada);
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        return false;
    }

    protected Franja mapearAPersistencia(FranjaHorarioDTOPeticion dto) {
        Horario horario = repoHorario.findById(dto.getHorarioId()).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el horario laboral diario con fecha: " + dto.getHorarioId()));

        Franja f = new Franja();
        f.setHorario(horario);
        f.setHoraInicio(dto.getHoraInicio());
        f.setHoraFin(dto.getHoraFin());
        if (dto.getBarberos() != null && !dto.getBarberos().isEmpty()) {
            List<Barbero> barberos = dto.getBarberos().stream()
                    .map(barberoService::findByIdInt)
                    .toList();
            f.setBarberos(barberos);
        }

        return f;
    }

    protected FranjaHorarioDTORespuesta mapearARespuesta(Franja f) {
        FranjaHorarioDTORespuesta dto = new FranjaHorarioDTORespuesta();
        dto.setId(f.getId());
        dto.setHorarioId(f.getHorario().getId());
        dto.setHoraInicio(f.getHoraInicio());
        dto.setHoraFin(f.getHoraFin());
        if (f.getBarberos() != null && !f.getBarberos().isEmpty()) {
            List<String> barberosIds = f.getBarberos()
                    .stream()
                    .map(Barbero::getId)
                    .toList();

            dto.setBarberos(barberosIds);
        }
        return dto;
    }

    private void validarFranja(LocalDate horarioId, LocalTime inicio, LocalTime fin, Integer idIgnorar) {
        // Validación de orden
        if (!inicio.isBefore(fin)) {
            throw new ReglaNegocioExcepcion("La hora inicio debe ser menor que la hora fin.");
        }

        // Validar rango permitido por día
        DayOfWeek dia = horarioId.getDayOfWeek();
        LocalTime limiteInicio = LocalTime.of(8, 0);
        LocalTime limiteFin;

        switch (dia) {
            case SATURDAY -> limiteFin = LocalTime.of(13, 0);
            case SUNDAY -> throw new ReglaNegocioExcepcion("No se permiten horarios los domingos.");
            default -> limiteFin = LocalTime.of(20, 0);
        }

        if (inicio.isBefore(limiteInicio) || fin.isAfter(limiteFin)) {
            throw new ReglaNegocioExcepcion("El horario permitido para " + dia + " es de " + limiteInicio + " a " + limiteFin + ". La franja enviada (" + inicio + " - " + fin + ") está fuera del rango.");
        }

        // Validación duplicado
        boolean existeExacta = repoFranja.existsByHorario_IdAndHoraInicioAndHoraFin(horarioId, inicio, fin);
        if (existeExacta && idIgnorar == null) {
            throw new ReglaNegocioExcepcion("Ya existe una franja con hora inicio " + inicio + " y hora fin " + fin + ".");
        }

        // Validación solapamiento
        boolean solapa = repoFranja.existsOverlapping(horarioId, inicio, fin);
        if (solapa) {
            throw new ReglaNegocioExcepcion("La franja " + inicio + " - " + fin + " se superpone con otra existente.");
        }
    }

    private boolean verificarDisponibilidadEnFranjas(String barberoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        if (!repoFranja.existsByBarberoAndHorario(barberoId, fecha)) {
            return false;
        }

        List<Franja> franjas = repoFranja.findFranjasBarberoDesde(barberoId, fecha, LocalTime.MIN)
                .stream()
                .filter(f -> f.getHorario().getId().equals(fecha))
                .sorted(Comparator.comparing(Franja::getHoraInicio))
                .toList();

        if (franjas.isEmpty()) {
            return false;
        }

        LocalTime tiempoActual = horaInicio;

        for (Franja franja : franjas) {
            if (franja.getHoraFin().isBefore(tiempoActual)) {
                continue;
            }

            if (franja.getHoraInicio().isAfter(tiempoActual)) {
                return false;
            }

            if (franja.getHoraFin().isAfter(tiempoActual)) {
                tiempoActual = franja.getHoraFin();
            }

            if (!tiempoActual.isBefore(horaFin)) {
                return true;
            }
        }

        return false;
    }
}
