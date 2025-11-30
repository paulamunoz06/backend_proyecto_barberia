package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import org.springframework.http.ResponseEntity;

public interface IUsuarioService {
   ResponseEntity<?> registerBarbero(BarberoUsuarioDTOPeticion signUpRequest);
   ResponseEntity<?> registerCiente(ClienteUsuarioDTOPeticion signUpRequest);
}
