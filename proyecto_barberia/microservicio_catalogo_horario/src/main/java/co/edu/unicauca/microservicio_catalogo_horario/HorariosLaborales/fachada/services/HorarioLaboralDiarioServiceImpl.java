package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos.FranjaHorarioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos.HorarioLaboralDiarioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Horario;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.AdministradorRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HorarioLaboralDiarioServiceImpl implements IHorarioLaboralDiarioService {
    @Autowired
    private HorarioLaboralDiarioRepository repoHorario;

    @Autowired
    private AdministradorRepository repoAdmin;

    @Autowired
    private FranjaHorarioRepository repoFranja;

    @Autowired
    private FranjaHorarioServiceImpl franjaService;

    @Override
    public HorarioLaboralDiarioDTORespuesta findById(LocalDate fecha) {
        Horario horario = repoHorario.findById(fecha).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el horario con fecha: " + fecha));
        return mapearARespuesta(horario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FranjaHorarioDTORespuesta> findByBarbero(LocalDate horarioId, String barberoId) {
        Horario horario = repoHorario.findById(horarioId).orElseThrow(() -> new ReglaNegocioExcepcion("No existe un horario laboral diario con fecha: " + horarioId));

        List<Franja> franjasDelBarbero = horario.getFranjas().stream()
                .filter(f -> f.getBarberos().stream().anyMatch(b -> b.getId().equals(barberoId)))
                .sorted(Comparator.comparing(Franja::getHoraInicio))
                .toList();

        if (franjasDelBarbero.isEmpty()) {
            throw new ReglaNegocioExcepcion("No hay actividades confirmadas para hoy");
        }

        return franjasDelBarbero
                .stream()
                .map(franjaService::mapearARespuesta)
                .toList();
    }

    @Override
    @Transactional
    public HorarioLaboralDiarioDTORespuesta save(HorarioLaboralDiarioDTOPeticion horarioDiario) {
        LocalDate hoy = LocalDate.now();

        if (horarioDiario.getId().isBefore(hoy)) {
            throw new ReglaNegocioExcepcion("No se pueden crear horarios en fechas anteriores a hoy.");
        }

        if (repoHorario.existsById(horarioDiario.getId())) {
            throw new ReglaNegocioExcepcion("Ya existe un horario para la fecha: " + horarioDiario.getId());
        }

        if (!horarioDiario.getFranjas().isEmpty()) {
            throw new ReglaNegocioExcepcion("Al crear un horario no se deben asignar barberos");
        }

        LocalDate fechaLimite = hoy.plusDays(20);
        if (horarioDiario.getId().isAfter(fechaLimite)) {
            throw new ReglaNegocioExcepcion("No se pueden crear horarios con más de 20 días de anticipación.");
        }

        DayOfWeek dia = horarioDiario.getId().getDayOfWeek();
        if (dia == DayOfWeek.SUNDAY) {
            throw new ReglaNegocioExcepcion("No se puede crear un horario para los domingos.");
        }

        Administrador admin = repoAdmin.findById(horarioDiario.getIdAdministrador()).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el administrador con ID: " + horarioDiario.getIdAdministrador()));

        Horario horario = new Horario();
        horario.setId(horarioDiario.getId());
        horario.setAdministrador(admin);
        horario.setFranjas(new ArrayList<>());

        Horario horarioGuardado = repoHorario.save(horario);

        crearFranjasAutomaticas(horarioGuardado);

        repoFranja.saveAll(horarioGuardado.getFranjas());

        return mapearARespuesta(horarioGuardado);
    }

    @Override
    @Transactional
    public HorarioLaboralDiarioDTORespuesta update(LocalDate fecha, HorarioLaboralDiarioDTOPeticion horario) {
        if (!fecha.equals(horario.getId())) {
            throw new RuntimeException("El ID del path no coincide con el ID del horario");
        }
        LocalDate hoy = LocalDate.now();
        if (fecha.isBefore(hoy)) {
            throw new ReglaNegocioExcepcion("No se pueden modificar horarios en fechas anteriores a hoy.");
        }

      /*  //verificar cada barbero, si tiene turnos para ese dia no se puede modificar
        if(){

        }*/

        Horario existente = repoHorario.findById(fecha).orElseThrow(() -> new ReglaNegocioExcepcion("No existe un horario laboral diario con fecha: " + fecha));
        Administrador admin = repoAdmin.findById(horario.getIdAdministrador()).orElseThrow(() -> new ReglaNegocioExcepcion("No existe el administrador con ID: " + horario.getIdAdministrador()));
        existente.setAdministrador(admin);

        if (!existente.getId().equals(horario.getId())) {
            throw new ReglaNegocioExcepcion("No se modificar la fecha de un horario");
        }

        for ( FranjaHorarioDTORespuesta fDto : horario.getFranjas()) {
            if (fDto.getId() != null) {
                franjaService.update(fDto.getId(), fDto);

            } else {
                Franja nueva = new Franja();
                nueva.setHoraInicio(fDto.getHoraInicio());
                nueva.setHoraFin(fDto.getHoraFin());
                nueva.setHorario(existente);

                existente.getFranjas().add(nueva);
            }
        }

        Horario actualizado = repoHorario.save(existente);
        return mapearARespuesta(actualizado);
    }

    private HorarioLaboralDiarioDTORespuesta mapearARespuesta(Horario entidad) {

        HorarioLaboralDiarioDTORespuesta dto = new HorarioLaboralDiarioDTORespuesta();
        dto.setId(entidad.getId());

        List<FranjaHorarioDTORespuesta> listaFranjas =
                entidad.getFranjas() == null ? List.of() :
                        entidad.getFranjas()
                                .stream()
                                .map(franjaService::mapearARespuesta)
                                .toList();
        dto.setFranjas(listaFranjas);

        return dto;
    }

    private void crearFranjasAutomaticas(Horario horario) {

        DayOfWeek dia = horario.getId().getDayOfWeek();

        if (dia == DayOfWeek.SUNDAY) {
            throw new ReglaNegocioExcepcion("No se permite crear horarios los domingos.");
        }

        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime limiteFin = (dia == DayOfWeek.SATURDAY)
                ? LocalTime.of(13, 0)
                : LocalTime.of(20, 0);

        LocalTime actual = inicio;

        while (actual.isBefore(limiteFin)) {
            if (!(actual.equals(LocalTime.of(13, 0)))) {
                Franja f = new Franja();
                f.setHorario(horario);
                f.setHoraInicio(actual);
                f.setHoraFin(actual.plusHours(1));
                f.setBarberos(new ArrayList<>());
                horario.getFranjas().add(f);
            }
            actual = actual.plusHours(1);
        }
    }
}
