package co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Cliente.fachada.DTOs.ClienteDTORespuesta;

import java.util.List;

public interface IClienteService {
    List<ClienteDTORespuesta> findAll();

    ClienteDTORespuesta findById(String id);

    String obtenerCorreoPorId(String clienteId);

    ClienteDTORespuesta save(ClienteDTOPeticion servicio);

    ClienteDTORespuesta update(String id, ClienteDTOPeticion servicio);

    boolean delete(String id);
}
