package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.services;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services.BarberoServiceImpl;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services.FranjaHorarioServiceImpl;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services.HorarioLaboralDiarioServiceImpl;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services.ServicioServiceImpl;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AsignarProximidad implements AsignarBarbero {
    @Autowired
    private BarberoServiceImpl barberoService;

    @Autowired
    private HorarioLaboralDiarioServiceImpl horarioLaboralDiarioService;

    @Autowired
    private FranjaHorarioServiceImpl franjaService;

    @Autowired
    private ServicioServiceImpl servicioService;

    @Override
    public List<BarberoFranjaDTORespuesta> elegirBarbero(BarberoFranjaDTOPeticion peticion) {
        horarioLaboralDiarioService.findById(peticion.getFecha());
        List<BarberoFranjaDTORespuesta> respuestas = new ArrayList<>();
        LocalDate fechaBusqueda = peticion.getFecha();
        final LocalTime horaFinJornada = (fechaBusqueda.getDayOfWeek() == DayOfWeek.SATURDAY)
                ? LocalTime.of(13, 0)
                : LocalTime.of(20, 0);

        LocalTime horaBaseInicio = LocalTime.of(8, 0);

        while (!horaBaseInicio.isAfter(horaFinJornada)) {
            BarberoFranjaDTORespuesta respuesta = new BarberoFranjaDTORespuesta();
            respuesta.setBarberoIds(new ArrayList<>());
            respuesta.setFranjas(new HashMap<>());

            LocalTime horaInicio = horaBaseInicio;
            boolean falloEnCadena = false;

            for (Integer servicioId : peticion.getServicioBarberoIds().keySet()) {
                List<BarberoDTORespuesta> barberosCapacitados = barberoService.findService(servicioId);

                if (barberosCapacitados.isEmpty()) {
                    throw new ReglaNegocioExcepcion("No hay barberos capacitados para el servicio " + servicioId);
                }

                BarberoDTORespuesta barberoElegido = seleccionarBarberoPorProximidad(barberosCapacitados, servicioId, fechaBusqueda, horaInicio);
                if (barberoElegido == null) {
                    falloEnCadena = true;
                    break;
                }

                Servicio servicio = servicioService.findByIdInt(servicioId);
                int duracionTotal = 10 + servicio.getDuracion() + servicio.getPreparacion();
                LocalTime horaFin = horaInicio.plusMinutes(duracionTotal);

                respuesta.getBarberoIds().add(barberoElegido.getId());
                respuesta.getFranjas().put(horaInicio,horaFin);

                horaInicio = horaFin;
            }

            if (!falloEnCadena) {
                respuestas.add(respuesta);
            }

            horaBaseInicio = horaBaseInicio.plusMinutes(10);
            if (horaBaseInicio.isAfter(horaFinJornada)) {
                break;
            }
        }

        if (respuestas.isEmpty()) {
            throw new ReglaNegocioExcepcion("No se encontró NINGUNA combinación válida de servicios consecutivos durante la jornada del " + fechaBusqueda);
        }
        return respuestas;
    }

    private BarberoDTORespuesta seleccionarBarberoPorProximidad(List<BarberoDTORespuesta> barberos, Integer servicioId, LocalDate fecha, LocalTime horaInicio) {
        for (BarberoDTORespuesta b : barberos) {
            Servicio servicio = servicioService.findByIdInt(servicioId);
            int duracion = 10 + servicio.getDuracion() + servicio.getPreparacion();
            LocalTime horaFin = horaInicio.plusMinutes(duracion);

            boolean disponible = franjaService.tieneDuracionContinua(b.getId(), fecha, horaInicio, horaFin);
            if (disponible) {
                return b;
            }
        }
        return null;
    }

}

