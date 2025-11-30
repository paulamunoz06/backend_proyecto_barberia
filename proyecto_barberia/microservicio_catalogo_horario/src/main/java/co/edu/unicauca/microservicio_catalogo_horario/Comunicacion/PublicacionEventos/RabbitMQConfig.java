package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos;

import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;

@Configuration
public class RabbitMQConfig {

    public static final String DELETE_TURNOS_BARBERO_QUEUE = "deleteTurnosBarberoQueue";
    public static final String DELETE_TURNOS_SERVICIO_QUEUE = "deleteTurnosServicioQueue";
    public static final String NOTIFICAR_CLIENTES_QUEUE = "notificarClientesQueue";

    @Bean
    public Queue deleteTurnosBarberoQueue() {
        return new Queue(DELETE_TURNOS_BARBERO_QUEUE, true);
    }

    @Bean
    public Queue deleteTurnosServiciosQueue() {
        return new Queue(DELETE_TURNOS_SERVICIO_QUEUE, true);
    }

    @Bean
    public Queue notificarClientesQueue() {
        return new Queue(NOTIFICAR_CLIENTES_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(simpleMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }
}
