package co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos;

import co.edu.unicauca.microservicio_turnos_reservas.Email.NotificacionesCliente;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios.TurnoServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventListener {

    private final TurnoServiceImpl turnoService;

    @Autowired
    private NotificacionesCliente notificacionesCliente;

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

    @RabbitListener(queues = "notificarCancelacionBarberoQueue")
    public void notificarMensajeCancelacionBarbero(String mensaje) {
        NotificacionEvento notificacion = convertirStringNotificacion(mensaje);
        notificacionesCliente.notificarCancelacionBarbero(notificacion.getCorreoCliente(), notificacion.getNombreBarbero(), notificacion.getNombreServicio(), notificacion.getFecha());
    }

    @RabbitListener(queues = "notificarCancelacionServicioQueue")
    public void otificarMensajeCancelacionServicio(String mensaje) {
        NotificacionEvento notificacion = convertirStringNotificacion(mensaje);
        notificacionesCliente.notificarCancelacionServicio(notificacion.getCorreoCliente(), notificacion.getNombreBarbero(), notificacion.getNombreServicio(), notificacion.getFecha());
    }

    private NotificacionEvento convertirStringNotificacion(String mensaje) {
        String[] partes = mensaje.split(",");

        if (partes.length >= 4) {
            String correo = partes[0];
            String nombreBarbero = partes[1];
            String nombreServicio = partes[2];
            String fecha = partes[3];

            // Crear entidad o procesar
            NotificacionEvento notificacion = new NotificacionEvento(correo, nombreBarbero, nombreServicio, fecha);
            return notificacion;
        }
        throw new RuntimeException("Error al convertir notificacion: " + mensaje);
    }
}