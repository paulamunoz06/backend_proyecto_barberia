package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs.FranjaHorarioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IFranjaHorarioService {
    List<FranjaHorarioDTORespuesta> findHorario(LocalDate horarioId);

    List<FranjaHorarioDTORespuesta> findBarbero(String barberoId);

    List<FranjaHorarioDTORespuesta> findBarberoDesdeHora(String barberoId, LocalDate fecha, LocalTime hora);

    Boolean verificarBarberoDesdeHora(String barberoId, LocalDate fecha, LocalTime hora);

    boolean tieneDuracionContinua(String barberoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    FranjaHorarioDTORespuesta findById(Integer id);

    FranjaHorarioDTORespuesta save(FranjaHorarioDTOPeticion franja);

    FranjaHorarioDTORespuesta update(Integer id, FranjaHorarioDTORespuesta franja);

    boolean delete(Integer id);
}
