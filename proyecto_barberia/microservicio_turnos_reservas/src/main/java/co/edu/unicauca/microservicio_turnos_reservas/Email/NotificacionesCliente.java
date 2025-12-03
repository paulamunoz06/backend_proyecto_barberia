package co.edu.unicauca.microservicio_turnos_reservas.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificacionesCliente {
    @Autowired
    private EmailService emailService;

    public void notificarCancelacionBarbero(String correoCliente, String nombreBarbero, String nombreServicio, String fecha) {
        String asunto = "Notificación de cancelación de servicio";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su cita programada con el barbero " + nombreBarbero
                + " para el servicio de " + nombreServicio
                + " ha sido cancelada.\n"
                + "Fecha de la cita: " + fecha + ".\n\n"
                + "Lamentamos los inconvenientes que esta situación pueda generar. "
                + "Con gusto podrá reprogramar su cita en el momento que mejor se ajuste a su disponibilidad.\n\n"
                + "Agradecemos su comprensión.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarCancelacionServicio(String correoCliente, String nombreBarbero, String nombreServicio, String fecha) {
        String asunto = "Notificación de cancelación de servicio";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su cita para el servicio de " + nombreServicio
                + " prevista para la fecha " + fecha
                + " ha sido cancelada.\n\n"
                + "Lamentamos los inconvenientes ocasionados y le invitamos a programar nuevamente el servicio cuando le sea conveniente.\n\n"
                + "Agradecemos su comprensión.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarCancelacionDemora(String correoCliente, String fecha, String hora) {
        String asunto = "Notificación de cancelación de servicio por demora";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su cita programada para el día "
                + fecha + " a las " + hora
                + " ha sido cancelada debido al retraso de su parte.\n\n"
                + "Con gusto podremos asistirle para reprogramar la cita en el horario que mejor se ajuste a sus necesidades.\n\n"
                + "Agradecemos su confianza y preferencia.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarCambioEstado(String correoCliente, String estado, String fecha, String hora) {
        String asunto = "Actualización del estado de su servicio";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que el estado de su cita programada para el día "
                + fecha + " a las " + hora
                + " ha cambiado a: " + estado + ".\n\n"
                + "Agradecemos su confianza y preferencia.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarEliminacionTurno(String correoCliente, String fecha, String hora) {
        String asunto = "Notificación de cancelación de su turno";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su turno programado para el día "
                + fecha + " a las " + hora
                + " ha sido eliminado.\n\n"
                + "Con gusto podrá solicitar un nuevo turno en el horario que mejor se ajuste a su disponibilidad.\n\n"
                + "Agradecemos su confianza y preferencia.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarEliminacionReserva(String correoCliente, String fecha) {
        String asunto = "Notificación de cancelación de su reserva";
        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su reserva programada para la fecha " + fecha
                + ", ha sido eliminada.\n\n"
                + "Con gusto podrá realizar nuevamente la reserva en el momento que mejor se ajuste a su disponibilidad.\n\n"
                + "Agradecemos su confianza y preferencia.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }
}
