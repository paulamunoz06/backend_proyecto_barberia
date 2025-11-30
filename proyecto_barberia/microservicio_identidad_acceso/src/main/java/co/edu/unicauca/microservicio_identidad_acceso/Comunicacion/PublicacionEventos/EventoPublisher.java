package co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.PublicacionEventos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBarberoRegistrado(Object payload) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.UPDATEPROJECT_QUEUE, "personas.barbero.registrado", payload);
    }
}