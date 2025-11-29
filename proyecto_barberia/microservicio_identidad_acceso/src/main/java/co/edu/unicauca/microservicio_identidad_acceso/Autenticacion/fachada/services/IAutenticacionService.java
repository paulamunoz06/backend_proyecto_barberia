package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.LoginDTOSolicitud;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.LoginDTORespuesta;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.ValidacionDTORespuesta;
import co.edu.unicauca.microservicio_identidad_acceso.Seguridad.services.UserDetailsImpl;

public interface IAutenticacionService {
   LoginDTORespuesta authenticateUser(LoginDTOSolicitud loginRequest);
   ValidacionDTORespuesta validarAcceso(String path, String method, UserDetailsImpl userDetails);
}
