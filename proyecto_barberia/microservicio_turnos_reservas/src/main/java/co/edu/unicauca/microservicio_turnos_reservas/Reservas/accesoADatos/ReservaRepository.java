package co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByClienteId(String clienteId);
}