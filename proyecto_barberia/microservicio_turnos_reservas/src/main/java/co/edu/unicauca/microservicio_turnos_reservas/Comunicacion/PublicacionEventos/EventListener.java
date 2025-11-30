package co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.TurnoServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private final TurnoServiceImpl turnoService;

    public EventListener(TurnoServiceImpl turnoService) {
        this.turnoService = turnoService;
    }

    @RabbitListener(queues = "deleteTurnosBarberoQueue")
    public void eliminarTurnosBarbero(String barberoId) {
        System.out.println("Solicitud de eliminación de turnos del barbero: " + barberoId);

        try {
            turnoService.eliminarTurnosActivosPorBarbero(barberoId);
            System.out.println("Turnos activos eliminados para el barbero " + barberoId);

        } catch (Exception e) {
            System.err.println("Error al eliminar turnos para el barbero " + barberoId + ": " + e.getMessage());
        }
    }

    @RabbitListener(queues = "deleteTurnosServicioQueue")
    public void eliminarTurnosServicio(Integer servicioId) {
        System.out.println("Solicitud de eliminación de turnos con el servicio: " + servicioId);

        try {
            turnoService.eliminarTurnosActivosPorServicio(servicioId);
            System.out.println("Turnos activos eliminados del servicio " + servicioId);

        } catch (Exception e) {
            System.err.println("Error al eliminar turnos con el servicio " + servicioId + ": " + e.getMessage());
        }
    }

    @RabbitListener(queues = "notificarClientesQueue")
    public void notificarMensajeCliente(NotificacionDTO mensaje) {

    }

    @RabbitListener(queues = "notificarAdministradorIncidenciaQueue")
    public void notificarMensajeAdministrador(NotificacionDTO mensaje) {

    }
}