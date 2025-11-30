package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Component
public class TurnoServiceClient {
    private final WebClient webClient;

    public TurnoServiceClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://turnos-reservas-service:5002")
                .build();
    }

    public boolean verificarDisponibilidadBarbero(String barberoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        try {
            Boolean disponible = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/turno/barbero/{barberoId}/disponibilidad")
                            .queryParam("fecha", fecha.toString())
                            .queryParam("horaInicio", horaInicio.toString())
                            .queryParam("horaFin", horaFin.toString())
                            .build(barberoId))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("Error en parámetros: " + errorBody)));
                    })
                    .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("Error del servidor: " + errorBody)));
                    })
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(disponible);

        } catch (Exception e) {
            System.err.println("Error verificando disponibilidad del barbero " + barberoId + " para " + fecha + " " + horaInicio + "-" + horaFin + ": " + e.getMessage());
            return false;
        }
    }
}