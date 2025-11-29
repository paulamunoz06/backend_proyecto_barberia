package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;

import java.util.List;

public interface ITurnoService {
    List<TurnoDTORespuesta> findAll();

    List<TurnoDTORespuesta> findByBarberoId(String id);

    List<TurnoDTORespuesta> findByReservaId(Integer reservaId);

    List<TurnoDTORespuesta> findByBarberoIdActivos(String id);

    TurnoDTORespuesta findById(Integer id);

    TurnoDTORespuesta save(TurnoDTOPeticion servicio);

    TurnoDTORespuesta update(Integer id, TurnoDTORespuesta servicio);

    boolean delete(Integer id);
}
