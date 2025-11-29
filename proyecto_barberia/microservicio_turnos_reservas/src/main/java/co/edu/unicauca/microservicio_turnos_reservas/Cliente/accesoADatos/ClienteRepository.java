package co.edu.unicauca.microservicio_turnos_reservas.Cliente.accesoADatos;

import co.edu.unicauca.microservicio_turnos_reservas.Cliente.modelos.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}