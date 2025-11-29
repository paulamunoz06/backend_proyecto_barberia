package co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTOPeticionBarbero;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.fachada.DTOs.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Turno;

import java.time.LocalTime;
import java.util.List;

public interface ITurnoService {
    List<TurnoDTORespuesta> findAll();

    List<TurnoDTORespuesta> findByBarberoId(String id);

    List<TurnoDTORespuesta> findByReservaId(Integer reservaId);

    List<TurnoDTORespuesta> findByBarberoIdActivos(String id);

    TurnoDTORespuesta findById(Integer id);

    TurnoDTORespuesta save(TurnoDTOPeticion servicio);

    TurnoDTORespuesta saveBarbero(TurnoDTOPeticionBarbero servicio);

    TurnoDTORespuesta update(Integer id, TurnoDTORespuesta servicio);

    LocalTime encontrarHora(TurnoDTORespuesta turno);

    boolean delete(Integer id);
}
