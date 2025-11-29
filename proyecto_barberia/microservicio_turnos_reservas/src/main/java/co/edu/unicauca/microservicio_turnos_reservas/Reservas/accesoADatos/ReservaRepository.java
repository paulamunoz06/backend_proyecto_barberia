package co.edu.unicauca.microservicio_turnos_reservas.Reservas.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Reservas.modelos.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
}