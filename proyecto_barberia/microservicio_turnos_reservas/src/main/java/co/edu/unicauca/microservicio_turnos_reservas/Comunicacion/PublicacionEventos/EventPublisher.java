package co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.PublicacionEventos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarNotificacionCliente(NotificacionDTO mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICAR_CLIENTES_QUEUE,
                mensaje
        );
    }
}