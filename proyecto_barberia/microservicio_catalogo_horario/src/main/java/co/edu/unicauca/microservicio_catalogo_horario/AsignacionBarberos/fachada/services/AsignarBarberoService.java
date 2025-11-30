package co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.AsignacionBarberos.fachada.DTOs.BarberoFranjaDTORespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AsignarBarberoService {
    @Autowired
    private AsignarProximidad proximidad;

    @Autowired
    private AsignarMixta mixta;

    @Autowired
    private AsignarPreferencia preferencia;

    private AsignarBarbero estrategia;

    public List<BarberoFranjaDTORespuesta> ejecutarEstrategia(BarberoFranjaDTOPeticion peticion) {
        Collection<String> barberoIds = peticion.getServicioBarberoIds().values();

        if (barberoIds.stream().allMatch(id -> id.equals("0"))) {
            estrategia = proximidad;
        } else if (barberoIds.stream().anyMatch(id -> id.equals("0"))) {
            estrategia = mixta;
        }else{
            estrategia = preferencia;
        }

        return estrategia.elegirBarbero(peticion);
    }
}