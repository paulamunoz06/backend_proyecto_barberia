package co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos.BarberoRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos.OcupacionRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.fachada.DTOs.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.EstadoUsuario;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Ocupacion;
import co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos.EventPublisher;
import co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.PublicacionEventos.NotificacionDTO;
import co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.REST.TurnoDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Comunicacion.REST.TurnoServiceClient;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.EntidadYaExisteException;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.accesoADatos.FranjaHorarioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.HorariosLaborales.modelos.Franja;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.AdministradorRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.ServicioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services.IServicioService;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class BarberoServiceImpl implements IBarberoService {
    @Autowired
    private BarberoRepository repo;

    @Autowired
    private AdministradorRepository repoAdministrador;

    @Autowired
    private ServicioRepository repoServicio;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private FranjaHorarioRepository repoFranja;

    @Autowired
    private OcupacionRepository repoOcupacion;

    @Autowired
    private TurnoServiceClient turnoServiceClient;

    @Autowired
    private EventPublisher barberoEventPublisher;

    @Override
    public List<BarberoDTORespuesta> findAll() {
        List<Barbero> barberos = repo.findAll();
        List<BarberoDTORespuesta> respuesta = new ArrayList<>();
        for (Barbero b : barberos) {
            BarberoDTORespuesta dto = mapearARespuesta(b);
            respuesta.add(dto);
        }
        return respuesta;
    }

    @Override
    public List<BarberoDTORespuesta> findActive() {
        List<Barbero> barberos = repo.findByEstado(EstadoUsuario.ACTIVO);
        if(barberos.isEmpty()){
            throw new ReglaNegocioExcepcion("No existen barberos activos");
        }
        List<BarberoDTORespuesta> respuesta = new ArrayList<>();
        for (Barbero b : barberos) {
            BarberoDTORespuesta dto = mapearARespuesta(b);
            respuesta.add(dto);
        }
        return respuesta;
    }

    @Override
    public List<BarberoDTORespuesta> findService(Integer servicioId) {
        if (!repoServicio.existsById(servicioId)) {
            throw new EntidadNoExisteException("El servicio con ID " + servicioId + " no existe");
        }
        List<Barbero> barberos = repo.findBarberosActivosByServicioId(servicioId);
        if(barberos.isEmpty()){
            throw new ReglaNegocioExcepcion("No hay barberos capacitados para este servicio actualmente");
        }
        List<BarberoDTORespuesta> respuesta = new ArrayList<>();

        for (Barbero b : barberos) {
            respuesta.add(mapearARespuesta(b));
        }

        return respuesta;
    }

    @Override
    public BarberoDTORespuesta findById(String id) {
        Barbero b = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El barbero con la identificación " + id + " no existe"));
        return mapearARespuesta(b);
    }

    @Override
    public Barbero findByIdInt(String id) {
        return repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El barbero con la identificación " + id + " no existe"));
    }

    @Override
    public boolean barberoHaceServicio(String barberoId, Integer servicioId) {
        Barbero b = findByIdInt(barberoId);

        return b.getServiciosEspecializados()
                .stream()
                .anyMatch(s -> s.getId().equals(servicioId));
    }

    @Override
    @Transactional
    public BarberoDTORespuesta save(BarberoDTOPeticion barbero) {
        if (repo.existsById(barbero.getId())) {
            throw new EntidadYaExisteException("El barbero con ID " + barbero.getId() + " ya existe");
        }
        validarBarbero(barbero);
        Barbero entidad = mapearAPersistencia(barbero);
        Ocupacion ocupacion = repoOcupacion.getReferenceById(3);
        entidad.setOcupacion(ocupacion);
        Barbero guardado = repo.save(entidad);
        return mapearARespuesta(guardado);
    }

    @Override
    @Transactional
    public BarberoDTORespuesta update(String id, BarberoDTOPeticion barbero) {
        if (!id.equals(barbero.getId())) {
            throw new RuntimeException("El ID del path no coincide con el ID del barbero");
        }

        Barbero existente = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El barbero no existe"));
        if (!Objects.equals(barbero.getId(), existente.getId())) {
            throw new RuntimeException("No es posible modificar el id del barbero");
        }
        barbero.setId(existente.getId());
        validarBarbero(barbero);

        Ocupacion ocupacion = repoOcupacion.getReferenceById(3);
        existente.setOcupacion(ocupacion);
        existente.setNombre(barbero.getNombre());
        existente.setEmail(barbero.getEmail());
        existente.setTelefono(barbero.getTelefono());
        existente.setEstado(EstadoUsuario.valueOf(barbero.getEstado()));
        existente.setNacimiento(barbero.getNacimiento());
        existente.setFotografia(barbero.getFotografia());

        List<Servicio> servicios = new ArrayList<>();
        for (Integer idServicio : barbero.getServicios()) {
            Servicio s = servicioService.findByIdInt(idServicio);
            servicios.add(s);
        }
        existente.setServiciosEspecializados(servicios);
        Barbero actualizado = repo.save(existente);
        return mapearARespuesta(actualizado);
    }

    @Override
    public BarberoDTORespuesta updateOcupacion(String id, String ocupacionNombre) {
        Barbero barbero = repo.findById(id).orElseThrow(() -> new RuntimeException("El barbero con id " + id + " no existe"));
        Ocupacion ocupacion = repoOcupacion.findByNombre(ocupacionNombre).orElseThrow(() -> new RuntimeException("La ocupación '" + ocupacionNombre + "' no existe"));
        barbero.setOcupacion(ocupacion);
        Barbero actualizado = repo.save(barbero);
        return mapearARespuesta(actualizado);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (id == null) {
            throw new ReglaNegocioExcepcion("El ID es obligatorio");
        }

        Barbero barbero = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El barbero no existe"));

        if (barbero.getEstado().equals(EstadoUsuario.INACTIVO)) {
            return false;
        }

        List<TurnoDTORespuesta> tieneTurnosFuturos = turnoServiceClient.obtenerTurnosFuturosBarbero(id);
        if(!tieneTurnosFuturos.isEmpty()) {
            barberoEventPublisher.barberoEnviarSolicitudEliminarTurnos(id);

            for (TurnoDTORespuesta t : tieneTurnosFuturos) {
                NotificacionDTO notificacion = new NotificacionDTO(turnoServiceClient.obtenerCorreo(t.getBarberoId()),"Estimado usuario, le informamos que uno de los barberos asignados a su reserva no se encuentra disponible para la fecha programada. Le recomendamos reagendar el servicio para garantizar una adecuada atención.");
                barberoEventPublisher.enviarNotificacionClientes(notificacion);
            }
        }

        List<Franja> franjas = repoFranja.findByBarberos_Id(barbero.getId());
        for (Franja f : franjas) {
            f.getBarberos().remove(barbero);
        }
        repoFranja.saveAll(franjas);

        barbero.setEstado(EstadoUsuario.INACTIVO);
        repo.save(barbero);
        return true;
    }

    private void validarBarbero(BarberoDTOPeticion barbero) {
        // 1. Campos obligatorios
        if (barbero.getNombre() == null ||
                barbero.getId() == null ||
                barbero.getNacimiento() == null ||
                barbero.getTelefono() == null ||
                barbero.getEmail() == null ||
                barbero.getEstado() == null ||
                barbero.getServicios() == null ||
                barbero.getServicios().isEmpty()) {

            throw new ReglaNegocioExcepcion("Campos obligatorios sin diligenciar");
        }

        // 2. Validar nombre (2 a 50 caracteres)
        if (!barbero.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,50}$")) {
            throw new ReglaNegocioExcepcion("El nombre solo puede contener letras y no debe exceder 50 caracteres");
        }

        // 3. Validar identificación
        String idStr = barbero.getId().toString();
        if (!idStr.matches("^[0-9]{1,10}$")) {
            throw new ReglaNegocioExcepcion("La identificación debe contener solo números y máximo 10 dígitos");
        }

        // 4. Validar fecha de nacimiento
        LocalDate fecha = barbero.getNacimiento();

        if (fecha.isAfter(LocalDate.now())) {
            throw new ReglaNegocioExcepcion("La fecha de nacimiento no es válida");
        }

        int edad = Period.between(fecha, LocalDate.now()).getYears();

        if (edad < 18 || edad > 100) {
            throw new ReglaNegocioExcepcion("El barbero debe tener entre 18 y 100 años de edad");
        }

        // 5. Validar teléfono
        String tel = barbero.getTelefono().toString();
        if (!tel.matches("^3\\d{9}$")) {
            throw new ReglaNegocioExcepcion("El número de teléfono debe comenzar con 3 y contener 10 dígitos");
        }

        // 6. Validar email
        if (!barbero.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ReglaNegocioExcepcion("El correo electrónico ingresado no tiene un formato válido");
        }

        // 7. Validar que los servicios existan
        if (barbero.getServicios() != null && !barbero.getServicios().isEmpty()) {
            for (Integer sid : barbero.getServicios()) {
                try {
                    servicioService.findByIdInt(sid);
                } catch (EntidadNoExisteException e) {
                    throw new ReglaNegocioExcepcion("El servicio con ID " + sid + " no existe");
                }
            }
        }else{
            throw new ReglaNegocioExcepcion("El barbero no tiene servicios");
        }

        // 8. validar que id adminstrador exista
        if (!repoAdministrador.existsById(barbero.getIdAdministrador())) {
            throw new EntidadNoExisteException("El Administrador con ID " + barbero.getIdAdministrador() + " no existe");
        }
    }

    public BarberoDTORespuesta mapearARespuesta(Barbero b) {
        BarberoDTORespuesta dto = new BarberoDTORespuesta();
        dto.setId(b.getId());
        dto.setNombre(b.getNombre());
        dto.setEmail(b.getEmail());
        dto.setTelefono(b.getTelefono());
        dto.setEstado(b.getEstado().toString());
        dto.setNacimiento(b.getNacimiento());
        dto.setOcupacion(b.getOcupacion().getNombre());
        dto.setFotografia(b.getFotografia());
        List<Integer> idsServicios = b.getServiciosEspecializados()
                .stream()
                .map(Servicio::getId)
                .toList();

        dto.setServicios(idsServicios);
        return dto;
    }

    private Barbero mapearAPersistencia(BarberoDTOPeticion dto) {
        Barbero b = new Barbero();
        b.setId(dto.getId());
        b.setAdministrador(repoAdministrador.getReferenceById(dto.getIdAdministrador()));
        b.setNombre(dto.getNombre());
        b.setEmail(dto.getEmail());
        b.setTelefono(dto.getTelefono());
        b.setEstado(EstadoUsuario.valueOf(dto.getEstado()));
        b.setNacimiento(dto.getNacimiento());
        b.setFotografia(dto.getFotografia());
        List<Servicio> servicios = new ArrayList<>();
        for (Integer idServicio : dto.getServicios()) {
            Servicio s = servicioService.findByIdInt(idServicio);
            servicios.add(s);
        }
        b.setServiciosEspecializados(servicios);
        return b;
    }
}
