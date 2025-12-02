package co.edu.unicauca.microservicio_turnos_reservas.Comunicacion.REST;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ServicioDTORespuesta;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public boolean validarTrabajoDiaBarbero(String id, LocalDateTime horaInicioBusqueda) {
        Boolean resultado = webClient.post()
                .uri("/api/franja/barbero/{id}/{horaInicioBusqueda}", id, horaInicioBusqueda)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return resultado;
    }

    public boolean validarBarberoHaceServicio(String idBarbero, Integer idServicio) {
        Boolean resultado = webClient.get()
                .uri("/api/barbero/{id}/verificar/{idServicio}", idBarbero, idServicio)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return resultado;
    }

    public boolean validarDuracionContinua(String id, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        Boolean resultado = webClient.get()
                .uri("/api/franja/barbero/{id}/{fecha}/{inicio}/{fin}", id, fecha, inicio,fin)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return resultado;
    }

    public ServicioDTORespuesta buscarServicio(Integer servicioId) {
        try {
            return webClient.get()
                    .uri("/api/servicio/{id}", servicioId)
                    .retrieve()
                    .bodyToMono(ServicioDTORespuesta.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error consultando servicio " + servicioId + ": " + e.getMessage());
            return null;
        }
    }
}
