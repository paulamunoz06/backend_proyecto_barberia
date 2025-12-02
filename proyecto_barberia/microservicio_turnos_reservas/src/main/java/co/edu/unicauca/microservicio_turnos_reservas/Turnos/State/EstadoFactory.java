package co.edu.unicauca.microservicio_turnos_reservas.Turnos.State;

import co.edu.unicauca.microservicio_turnos_reservas.Turnos.accesoADatos.EstadoRepository;
import co.edu.unicauca.microservicio_turnos_reservas.Turnos.modelos.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoFactory {

    private final EstadoRepository estadoRepository;

    @Autowired
    public EstadoFactory(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public Estado getEstado(String nombre) {
        return estadoRepository.findByNombre(nombre).orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + nombre));
    }
}