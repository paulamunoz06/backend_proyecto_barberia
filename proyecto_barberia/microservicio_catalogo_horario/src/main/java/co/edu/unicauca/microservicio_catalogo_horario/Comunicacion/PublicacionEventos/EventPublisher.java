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

    public void enviarNotificacionClientes(NotificacionDTO mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICAR_CLIENTES_QUEUE,
                mensaje
        );
    }
}