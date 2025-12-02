package co.edu.unicauca.microservicio_turnos_reservas.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacionesCliente {
    @Autowired
    private EmailService emailService;

    public void notificarCancelacionBarbero(String correoCliente, String nombreBarbero, String nombreServicio, String fecha) {
        String asunto = "Notificación de Cancelación de Cita";

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
        String asunto = "Notificación de Cancelación de Servicio";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su cita para el servicio de " + nombreServicio
                + " prevista para la fecha " + fecha
                + " ha sido cancelada.\n\n"
                + "Lamentamos los inconvenientes ocasionados y le invitamos a programar nuevamente el servicio cuando le sea conveniente.\n\n"
                + "Agradecemos su comprensión.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarCancelacionDemora(String correoCliente, String fecha, String hora) {
        String asunto = "Notificación de Cancelación por Demora";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que su cita programada para el día "
                + fecha + " a las " + hora
                + " ha sido cancelada debido a retrasos imprevistos.\n\n"
                + "Lamentamos sinceramente los inconvenientes ocasionados. "
                + "Con gusto podremos asistirle para reprogramar la cita en el horario que mejor se ajuste a sus necesidades.\n\n"
                + "Agradecemos su comprensión.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }

    public void notificarCambioEstado(String correoCliente, String estado, String fecha, String hora) {
        String asunto = "Actualización del Estado de su Cita";

        String mensaje = "Estimado(a) cliente,\n\n"
                + "Le informamos que el estado de su cita programada para el día "
                + fecha + " a las " + hora
                + " ha cambiado a: " + estado + ".\n\n"
                + "Le mantendremos informado(a) sobre cualquier actualización adicional relacionada con su cita.\n\n"
                + "Agradecemos su confianza y preferencia.";

        emailService.enviarCorreo(correoCliente, asunto, mensaje);
    }
}
