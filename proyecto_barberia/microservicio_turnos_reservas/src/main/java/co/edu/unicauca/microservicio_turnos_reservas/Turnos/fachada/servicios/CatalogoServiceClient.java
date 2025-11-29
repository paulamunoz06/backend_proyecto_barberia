package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogoServiceClient {

    private final WebClient webClient;

    public CatalogoServiceClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://catalogo-horario-service:5000")
                .build();
    }

    public boolean validarBarbero(String barberoId) {
        try {
            webClient.get()
                    .uri("/api/barbero/{id}", barberoId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Error validando barbero: " + e.getMessage());
            return false;
        }
    }

    public boolean validarServicio(Integer servicioId) {
        try {
            webClient.get()
                    .uri("/api/servicio/{id}", servicioId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Error validando servicio: " + e.getMessage());
            return false;
        }
    }
}
