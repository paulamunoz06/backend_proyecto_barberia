package co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST;

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

    public BarberoDTORespuesta buscarBarbero(String barberoId) {
        try {
            return webClient.get()
                    .uri("/api/barbero/{id}", barberoId)
                    .retrieve()
                    .bodyToMono(BarberoDTORespuesta.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error consultando barbero " + barberoId + ": " + e.getMessage());
            return null;
        }
    }
}
