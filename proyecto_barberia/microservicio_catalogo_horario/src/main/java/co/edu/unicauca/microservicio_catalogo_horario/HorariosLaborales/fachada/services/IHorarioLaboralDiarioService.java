package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.HorarioLaboralDiarioDTORespuesta;

import java.time.LocalDate;
import java.util.List;

public interface IHorarioLaboralDiarioService {

    HorarioLaboralDiarioDTORespuesta findById(LocalDate id);

    List<FranjaHorarioDTORespuesta> findByBarbero(LocalDate horarioId, String barberoId);

    HorarioLaboralDiarioDTORespuesta save(HorarioLaboralDiarioDTOPeticion horarioDiario);

    HorarioLaboralDiarioDTORespuesta update(LocalDate id, HorarioLaboralDiarioDTOPeticion horarioDiario);
}
