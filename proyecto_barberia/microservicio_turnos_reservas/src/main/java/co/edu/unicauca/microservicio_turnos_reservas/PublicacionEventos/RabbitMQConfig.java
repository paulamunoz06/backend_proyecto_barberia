package co.edu.unicauca.microservicio_turnos_reservas.PublicacionEventos;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CREATEBARBERO_QUEUE = "createBarberoQueue";
    public static final String UPDATEBARBERO_QUEUE = "updateBarberoQueue";
    public static final String DELETEBARBERO_QUEUE = "deleteProjectQueue";

    @Bean
    public Queue createBarberoQueue() {
        return new Queue(CREATEBARBERO_QUEUE, true);
    }

    @Bean
    public Queue updateBarberoQueue() {
        return new Queue(UPDATEBARBERO_QUEUE, true);
    }

    @Bean
    public Queue deleteProjectQueue() {
        return new Queue(DELETEBARBERO_QUEUE, true);
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

    /*
    public void enviarPostulacion(String mensaje) {
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.STUDENTPOSTULATION_QUEUE,
        mensaje
    );
}
     */

    /*

     */
}