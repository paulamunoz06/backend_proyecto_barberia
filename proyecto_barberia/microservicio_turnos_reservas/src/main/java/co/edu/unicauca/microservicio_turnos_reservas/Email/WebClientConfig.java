package co.edu.unicauca.microservicio_turnos_reservas.Email;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {
    @Bean
    public WebClient sendGridClient() {
        return WebClient.builder()
                .baseUrl("https://api.sendgrid.com/v3")
                .defaultHeader("Authorization", "Bearer TU_API_KEY")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
