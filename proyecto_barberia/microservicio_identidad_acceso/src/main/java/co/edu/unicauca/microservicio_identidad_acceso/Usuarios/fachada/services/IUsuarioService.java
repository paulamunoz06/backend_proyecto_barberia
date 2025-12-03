package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IUsuarioService {
   ResponseEntity<?> registerBarbero(BarberoUsuarioDTOPeticion signUpRequest, MultipartFile archivo);
   ResponseEntity<?> registerCiente(ClienteUsuarioDTOPeticion signUpRequest);
}
