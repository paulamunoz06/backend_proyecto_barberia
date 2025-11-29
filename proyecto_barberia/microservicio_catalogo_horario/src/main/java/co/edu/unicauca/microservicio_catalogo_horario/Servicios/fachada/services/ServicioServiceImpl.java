package co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.services;

import co.edu.unicauca.microservicio_catalogo_horario.Barberos.accesoADatos.BarberoRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Barberos.modelos.Barbero;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.EntidadNoExisteException;
import co.edu.unicauca.microservicio_catalogo_horario.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.AdministradorRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.CategoriaServicioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.accesoADatos.ServicioRepository;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTOPeticion;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.fachada.DTOs.ServicioDTORespuesta;
import co.edu.unicauca.microservicio_catalogo_horario.Servicios.modelos.Servicio;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioServiceImpl implements IServicioService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ServicioRepository repo;

    @Autowired
    private AdministradorRepository adminRepo;

    @Autowired
    private CategoriaServicioRepository categoriaRepo;

    @Autowired
    private BarberoRepository barberoRepo;

    @Override
    public List<ServicioDTORespuesta> findAll() {
        List<Servicio> servicios = repo.findAll();
        List<ServicioDTORespuesta> respuesta = new ArrayList<>();

        for (Servicio s : servicios) {
            respuesta.add(mapearARespuesta(s));
        }

        return respuesta;
    }

    @Override
    public ServicioDTORespuesta findById(Integer id) {
        Servicio s = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El servicio con ID " + id + " no existe"));

        return mapearARespuesta(s);
    }

    @Override
    public List<ServicioDTORespuesta> findByCategoria(Integer id) {
        List<Servicio> servicios = repo.findByCategoriaId(id);
        if(servicios.isEmpty()){
            throw new ReglaNegocioExcepcion("No hay servicios para esta categoria");
        }
        return servicios.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTORespuesta> findByIdBarbero(String id) {
        List<Servicio> servicios = repo.findByBarberoId(id);
        if(servicios.isEmpty()){
            throw new ReglaNegocioExcepcion("No hay servicios para este barbero");
        }
        return servicios.stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioDTORespuesta save(ServicioDTOPeticion dto) {
        validarServicio(dto);

        Servicio entidad = mapearAPersistencia(dto);

        Servicio guardado = repo.save(entidad);

        return mapearARespuesta(guardado);
    }

    @Override
    public ServicioDTORespuesta update(Integer id, ServicioDTOPeticion dto) {
        Servicio existente = repo.findById(id) .orElseThrow(() -> new EntidadNoExisteException("El servicio con ID " + id + " no existe"));

        validarServicio(dto);

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setDuracion(dto.getDuracion());
        existente.setPrecio(dto.getPrecio());
        existente.setPreparacion(dto.getPreparacion());
        existente.setAdministrador(adminRepo.getReferenceById(dto.getAdministradorId()));
        existente.setCategoria(categoriaRepo.getReferenceById(dto.getCategoriaId()));

        Servicio actualizado = repo.save(existente);
        return mapearARespuesta(actualizado);
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        Servicio s = repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El servicio con ID " + id + " no existe"));

        if (s.getBarberos() != null && !s.getBarberos().isEmpty()) {
            for (Barbero b : s.getBarberos()) {
                b.getServiciosEspecializados().remove(s);
                barberoRepo.save(b);
            }
            s.getBarberos().clear();
        }

        repo.delete(s);
        return true;
    }

    @Override
    public Servicio findByIdInt(Integer id) {
        return repo.findById(id).orElseThrow(() -> new EntidadNoExisteException("El servicio con ID " + id + " no existe"));
    }

    private void validarServicio(ServicioDTOPeticion dto) {

        // 1. Validar campos obligatorios
        if (dto.getNombre() == null || dto.getNombre().isBlank() ||
                dto.getDescripcion() == null || dto.getDescripcion().isBlank() ||
                dto.getDuracion() == null ||
                dto.getPrecio() == null ||
                dto.getPreparacion() == null ||
                dto.getAdministradorId() == null ||
                dto.getCategoriaId() == null) {

            throw new ReglaNegocioExcepcion("Campos obligatorios sin diligenciar");
        }

        // 2. Validar nombre (solo letras y hasta 50 caracteres)
        if (!dto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,50}$")) {
            throw new ReglaNegocioExcepcion("El nombre solo puede contener letras y no debe exceder 50 caracteres");
        }

        // 3. Validar descripción (hasta 255 caracteres)
        if (dto.getDescripcion().length() > 255) {
            throw new ReglaNegocioExcepcion("La descripción solo puede contener hasta 255 caracteres");
        }

        // 4. Validar duración (5 a 300 minutos)
        if (dto.getDuracion() < 5 || dto.getDuracion() > 300) {
            throw new ReglaNegocioExcepcion("La duración debe ser un valor numérico entre 5 y 300 minutos");
        }

        // 5. Validar precio (> 0)
        if (dto.getPrecio() <= 0) {
            throw new ReglaNegocioExcepcion("El precio debe ser un valor numérico mayor a cero");
        }

        // 6. Validar categoría existente
        if (!categoriaRepo.existsById(dto.getCategoriaId())) {
            throw new ReglaNegocioExcepcion("Debe elegir una categoría registrada y disponible en el sistema");
        }

        // 7. Validar administrador existente
        if (!adminRepo.existsById(dto.getAdministradorId())) {
            throw new EntidadNoExisteException("El administrador con ID " + dto.getAdministradorId() + " no existe");
        }
    }

    private ServicioDTORespuesta mapearARespuesta(Servicio s) {
        ServicioDTORespuesta dto = new ServicioDTORespuesta();
        dto.setServicioId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setDescripcion(s.getDescripcion());
        dto.setDuracion(s.getDuracion());
        dto.setPrecio(s.getPrecio());
        dto.setPreparacion(s.getPreparacion());
        dto.setAdministradorId(s.getAdministrador().getId());
        dto.setCategoriaId(s.getCategoria().getId());
        return dto;
    }

    private Servicio mapearAPersistencia(ServicioDTOPeticion dto) {
        Servicio s = new Servicio();
        s.setNombre(dto.getNombre());
        s.setDescripcion(dto.getDescripcion());
        s.setDuracion(dto.getDuracion());
        s.setPrecio(dto.getPrecio());
        s.setPreparacion(dto.getPreparacion());
        s.setAdministrador(adminRepo.getReferenceById(dto.getAdministradorId()));
        s.setCategoria(categoriaRepo.getReferenceById(dto.getCategoriaId()));
        return s;
    }


}
