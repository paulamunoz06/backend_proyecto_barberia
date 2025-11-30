package co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.REST;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    public List<TurnoDTORespuesta> obtenerTurnosFuturosBarbero(String barberoId) {
        try {
            List<TurnoDTORespuesta> turnos = webClient.get()
                    .uri("/api/turno/activas/barbero/{id}", barberoId)
                    .retrieve()
                    .bodyToFlux(TurnoDTORespuesta.class)
                    .collectList()
                    .block();

            if (turnos == null || turnos.isEmpty()) {
                return List.of(); // Lista vacía
            }

            LocalDate hoy = LocalDate.now();

            return turnos.stream()
                    .filter(t -> t.getFechaInicio().isAfter(hoy))
                    .toList();

        } catch (Exception e) {
            System.err.println("Error consultando turnos futuros del barbero " + barberoId + ": " + e.getMessage());
            return List.of();
        }
    }

    public List<TurnoDTORespuesta> obtenerTurnosFuturosServicios(Integer servicioId) {
        try {
            List<TurnoDTORespuesta> turnos = webClient.get()
                    .uri("/api/turno/activas/servicio/{id}", servicioId)
                    .retrieve()
                    .bodyToFlux(TurnoDTORespuesta.class)
                    .collectList()
                    .block();

            if (turnos == null || turnos.isEmpty()) {
                return List.of(); // Lista vacía
            }

            LocalDate hoy = LocalDate.now();

            return turnos.stream()
                    .filter(t -> t.getFechaInicio().isAfter(hoy))
                    .toList();

        } catch (Exception e) {
            System.err.println("Error consultando turnos futuros del servicio " + servicioId + ": " + e.getMessage());
            return List.of();
        }
    }

    public boolean tieneTurnosFecha(LocalDate fecha, String barberoId) {
        try {
            List<TurnoDTORespuesta> turnos = webClient.get()
                    .uri("/api/turno/activas/barbero/{barberoId}/{fecha}", barberoId, fecha)
                    .retrieve()
                    .bodyToFlux(TurnoDTORespuesta.class)
                    .collectList()
                    .block();

            if (turnos == null || turnos.isEmpty()) {
                return false;
            }

            LocalDate hoy = LocalDate.now();

            return turnos.stream().anyMatch(t ->
                    t.getFechaInicio().isAfter(hoy)
            );

        } catch (Exception e) {
            System.err.println("Error consultando turnos para el dia " + fecha + ": " + e.getMessage());
            return false;
        }
    }

    public String obtenerCorreo(String id) {
        try {
            String correo = webClient.get()
                    .uri("/api/cliente/{id}/correo", id)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return correo;

        } catch (Exception e) {
            System.err.println("Error consultando el correo del cliente con ID " + id + ": " + e.getMessage());
            return null;
        }
    }
}