package co.edu.unicauca.api_gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Clase de configuración que registra beans de WebClient
@Configuration
public class WebClientConfig {

    // Builder de WebClient con balanceo de carga para resolver servicios por nombre
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient identidadServiceClient(WebClient.Builder builder) {
        // Crea un WebClient específico para consumir el microservicio identidad-acceso-service.
        return builder.baseUrl("http://identidad-acceso-service:5001").build();
    }
}