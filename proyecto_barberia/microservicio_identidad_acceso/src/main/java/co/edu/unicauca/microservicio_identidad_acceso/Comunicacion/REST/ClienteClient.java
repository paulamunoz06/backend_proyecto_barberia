package co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteDTOPeticion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class ClienteClient {
    private final String baseUrl = "http://turnos-reservas-service:5002/api/cliente";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> crearPreliminar(ClienteDTOPeticion dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTOPeticion> request = new HttpEntity<>(dto, headers);
        try {
            return restTemplate.postForEntity(baseUrl, request, String.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }
}