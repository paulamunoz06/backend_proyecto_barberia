package co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.fachada.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class HorarioLaboralDiarioDTOPeticion {

    @NotNull(message = "La fecha (id) es obligatoria")
    private LocalDate id;

    @NotBlank(message = "El idAdministrador es obligatorio")
    private String idAdministrador;

    @NotNull(message = "La lista de franjas no puede ser nula, puede enviarse vacía")
    @Valid
    private List<FranjaHorarioDTORespuesta> franjas;
}

