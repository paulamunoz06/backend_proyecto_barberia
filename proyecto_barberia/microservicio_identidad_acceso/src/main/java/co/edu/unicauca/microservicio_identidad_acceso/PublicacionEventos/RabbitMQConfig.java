package co.edu.unicauca.microservicio_identidad_acceso.PublicacionEventos;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STUDENTPOSTULATION_QUEUE = "studentPostulationQueue";
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";
    public static final String CREATEPROJECT_QUEUE = "createProjectQueue";

    @Bean
    public Queue studentPostulationQueue() {
        return new Queue(STUDENTPOSTULATION_QUEUE, true);
    }

    @Bean
    public Queue createProjectQueue() {
        return new Queue(CREATEPROJECT_QUEUE, true);
    }

    @Bean
    public Queue updateProjectQueue() {
        return new Queue(UPDATEPROJECT_QUEUE, true);
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