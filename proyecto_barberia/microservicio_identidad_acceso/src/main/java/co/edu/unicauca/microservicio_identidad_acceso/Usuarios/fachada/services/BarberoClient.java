package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoDTOPeticion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class BarberoClient {
    private final String baseUrl = "http://catalogo-horario-service:5000/api/barbero";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> crearPreliminar(BarberoDTOPeticion dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BarberoDTOPeticion> request = new HttpEntity<>(dto, headers);
        try {
            return restTemplate.postForEntity(baseUrl, request, String.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }
}