package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void barberoEnviarSolicitudEliminarTurnos(String barberoId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELETE_TURNOS_BARBERO_QUEUE,
                barberoId
        );
    }

    public void servicioEnviarSolicitudEliminarTurnos(Integer servicioId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELETE_TURNOS_SERVICIO_QUEUE,
                servicioId
        );
    }

    public void enviarNotificacionCancelacionBarbero(NotificacionEvento notificacion) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICAR_CANCELACION_BARBERO_QUEUE,
                convertirNotificacionString(notificacion)
        );
    }

    public void enviarNotificacionCancelacionServicio(NotificacionEvento notificacion) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICAR_CANCELACION_SERVICIO_QUEUE,
                convertirNotificacionString(notificacion)
        );
    }

    private String convertirNotificacionString(NotificacionEvento notificacion) {
        return String.format("%s,%s,%s,%s",
                notificacion.getCorreoCliente(),
                notificacion.getNombreBarbero(),
                notificacion.getNombreServicio(),
                notificacion.getFecha());
    }
}