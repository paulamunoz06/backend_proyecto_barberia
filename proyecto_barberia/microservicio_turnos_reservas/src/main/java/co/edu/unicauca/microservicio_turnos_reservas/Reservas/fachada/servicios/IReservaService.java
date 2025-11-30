package co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.servicios;

import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTOPeticion;
import co.edu.unicauca.microservicio_turnos_reservas.Reservas.fachada.DTOs.ReservaDTORespuesta;

import java.util.List;

public interface IReservaService {
    List<ReservaDTORespuesta> findAll();

    ReservaDTORespuesta findById(Integer id);

    List<ReservaDTORespuesta> findByClienteId(String id);

    ReservaDTORespuesta save(ReservaDTOPeticion servicio);

    ReservaDTORespuesta update(Integer id, ReservaDTORespuesta servicio);

    boolean delete(Integer id);

    boolean cancelar(Integer id);
}
