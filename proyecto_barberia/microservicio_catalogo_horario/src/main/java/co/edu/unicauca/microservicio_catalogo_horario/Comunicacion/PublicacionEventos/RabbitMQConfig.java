package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    public static final String DELETE_TURNOS_BARBERO_QUEUE = "deleteTurnosBarberoQueue";
    public static final String DELETE_TURNOS_SERVICIO_QUEUE = "deleteTurnosServicioQueue";
    public static final String NOTIFICAR_CANCELACION_BARBERO_QUEUE = "notificarCancelacionBarberoQueue";
    public static final String NOTIFICAR_CANCELACION_SERVICIO_QUEUE = "notificarCancelacionServicioQueue";

    @Bean
    public Queue deleteTurnosBarberoQueue() {
        return new Queue(DELETE_TURNOS_BARBERO_QUEUE, true);
    }

    @Bean
    public Queue deleteTurnosServiciosQueue() {
        return new Queue(DELETE_TURNOS_SERVICIO_QUEUE, true);
    }

    @Bean
    public Queue notificarCancelacionBarberoQueue() {
        return new Queue(NOTIFICAR_CANCELACION_BARBERO_QUEUE, true);
    }

    @Bean
    public Queue notificarCancelacionServicioQueue() {
        return new Queue(NOTIFICAR_CANCELACION_SERVICIO_QUEUE, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}