package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.strategy;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoFranjaDTORespuesta;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AsignarMixta implements AsignarBarbero {
    @Autowired
    private HorarioLaboralDiarioServiceImpl horarioLaboralDiarioService;

    @Autowired
    private BarberoServiceImpl barberoService;

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
                String idBarbero = peticion.getServicioBarberoIds().get(servicioId);
                BarberoDTORespuesta barberoElegido = null;

                if (idBarbero.equals("0")) {
                    List<BarberoDTORespuesta> barberosCapacitados = barberoService.findService(servicioId);
                    if (barberosCapacitados.isEmpty()) {
                        throw new ReglaNegocioExcepcion("No hay barberos capacitados para el servicio " + servicioId);
                    }
                    barberoElegido = seleccionarBarbero(barberosCapacitados, servicioId, fechaBusqueda, horaInicio);
                } else {
                    BarberoDTORespuesta barbero = barberoService.findById(idBarbero);
                    if(!barberoService.barberoHaceServicio(barbero.getId(), servicioId)) {
                        throw new ReglaNegocioExcepcion("El barbero con id " + idBarbero + " no es especialista en el servicio " + servicioId);
                    }
                    List<BarberoDTORespuesta> barberosCapacitados = new ArrayList<>();
                    barberosCapacitados.add(barbero);
                    barberoElegido = seleccionarBarbero(barberosCapacitados, servicioId, fechaBusqueda, horaInicio);
                }

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

    private BarberoDTORespuesta seleccionarBarbero(List<BarberoDTORespuesta> barberos, Integer servicioId, LocalDate fecha, LocalTime horaInicio) {
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
