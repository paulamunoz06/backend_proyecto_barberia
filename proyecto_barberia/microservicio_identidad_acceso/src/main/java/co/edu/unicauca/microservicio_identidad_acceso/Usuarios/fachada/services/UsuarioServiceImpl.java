package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.BarberoClient;
import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.BarberoDTORespuesta;
import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.ClienteClient;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.EntidadYaExisteException;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos.RoleRepository;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos.UserRepository;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.CatalogoServiceClient;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.ERole;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.Role;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    BarberoClient barberoClient;

    @Autowired
    ClienteClient clienteClient;

    @Autowired
    CatalogoServiceClient categoriaClient;

    @Value("${file.upload.directory}")
    private String uploadDir;

    @Value("${file.upload.max-size}")
    private String maxSizeStr;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        crearDirectorioSiNoExiste("barberos");
    }

    @Override
    public ResponseEntity<?> registerBarbero(BarberoUsuarioDTOPeticion signUpRequest, MultipartFile archivo) {

        if (userRepository.existsByUsername(signUpRequest.getId())) {
            throw new EntidadYaExisteException("El usuario ya se encuentra registrado");
        }

        try {
            String nombreArchivo = null;
            String fotoUrl = null;

            // Guardar la imagen
            if (archivo != null && !archivo.isEmpty()) {
                nombreArchivo = guardarArchivo(archivo, "barberos");
                fotoUrl = baseUrl + "/api/barberos/imagen/" + nombreArchivo;
            }

            BarberoDTOPeticion barberoDTO = fromBarberoUsuarioToBarbero(signUpRequest);
            barberoDTO.setFotografia(nombreArchivo);
            ResponseEntity<String> resp = barberoClient.crearPreliminar(barberoDTO);

            if (resp.getStatusCode().is2xxSuccessful()) {
                Set<Role> roles = new HashSet<>();
                Role barberoRole = roleRepository.findByName(ERole.BARBERO).orElseThrow(() -> new ReglaNegocioExcepcion("El rol no existe"));
                roles.add(barberoRole);

                User user = new User(signUpRequest.getId(), encoder.encode(signUpRequest.getPassword()));
                user.setRoles(roles);
                userRepository.save(user);

                return ResponseEntity.status(201).body("Usuario y barbero creados exitosamente");
            } else {
                if (nombreArchivo != null) {
                    eliminarArchivo(nombreArchivo);
                }
                return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
            }

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar barbero: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> actualizarFotoBarbero(String idBarbero, MultipartFile nuevaFoto) {

        if (nuevaFoto == null || nuevaFoto.isEmpty()) {
            throw new ReglaNegocioExcepcion("Debe enviar un archivo de imagen válido.");
        }

        try {
            BarberoDTORespuesta barbero = categoriaClient.buscarBarbero(idBarbero);
            if(barbero == null) {
                throw new ReglaNegocioExcepcion("El barbero no existe");
            }

            if (barbero.getFotografia() != null) {
                String nombrePrevio = extraerNombreArchivo(barbero.getFotografia());
                eliminarArchivo("barberos/" + nombrePrevio);
            }

            String nuevoNombreArchivo = guardarArchivo(nuevaFoto, "barberos");
            String nuevaFotoUrl = baseUrl + "/api/barberos/imagen/" + nuevoNombreArchivo;

            return ResponseEntity.ok(nuevaFotoUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> eliminarFotoBarbero(String idBarbero) {

        try {
            BarberoDTORespuesta barbero = categoriaClient.buscarBarbero(idBarbero);
            if(barbero == null) {
                throw new ReglaNegocioExcepcion("El barbero no existe");
            }

            if (barbero.getFotografia() == null) {
                return ResponseEntity.status(400).body("El barbero no tiene fotografía registrada");
            }

            String nombreArchivo = extraerNombreArchivo(barbero.getFotografia());
            eliminarArchivo("barberos/" + nombreArchivo);

            return ResponseEntity.ok("Fotografía eliminada correctamente");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al eliminar fotografía: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> visualizarFotoBarbero(String nombre) {
        try {
            Path ruta = Paths.get(uploadDir, "barberos", nombre);

            if (!Files.exists(ruta)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imagen = Files.readAllBytes(ruta);

            return ResponseEntity.ok()
                    .header("Content-Type", Files.probeContentType(ruta))
                    .body(imagen);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al mostrar la imagen");
        }
    }

    @Override
    public ResponseEntity<?> registerCiente(ClienteUsuarioDTOPeticion signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getId())) {
            throw new EntidadYaExisteException("El usuario ya se encuentra registrado");
        }

        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.CLIENTE).orElseThrow(() -> new ReglaNegocioExcepcion("El rol no existe"));
        roles.add(modRole);

        ClienteDTOPeticion prelim = fromBarberoUsuarioToCliente(signUpRequest);
        ResponseEntity<String> resp = clienteClient.crearPreliminar(prelim);

        if (resp.getStatusCode().is2xxSuccessful()) {
            User user = new User(signUpRequest.getId(),encoder.encode(signUpRequest.getPassword()));
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.status(201).body("Usuario y cliente creados");
        } else {
            return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
        }
    }

    private String guardarArchivo(MultipartFile archivo, String tipoUsuario) throws IOException {
        Path uploadPath = Paths.get(uploadDir, tipoUsuario);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = uploadPath.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        return nombreArchivo;
    }

    private String extraerNombreArchivo(String url) {
        if (url == null) return null;
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private void eliminarArchivo(String rutaRelativa) throws IOException {
        Path archivoPath = Paths.get(uploadDir, rutaRelativa);
        Files.deleteIfExists(archivoPath);
    }

    private void crearDirectorioSiNoExiste(String subdirectorio) {
        try {
            Path dirPath = Paths.get(uploadDir, subdirectorio);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("Directorio creado: " + dirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorio " + subdirectorio + ": " + e.getMessage());
        }
    }

    private BarberoDTOPeticion fromBarberoUsuarioToBarbero(BarberoUsuarioDTOPeticion origen) {
        if (origen == null) {
            return null;
        }

        BarberoDTOPeticion destino = new BarberoDTOPeticion();
        destino.setNombre(origen.getNombre());
        destino.setId(origen.getId());
        destino.setNacimiento(origen.getNacimiento());
        destino.setServicios(origen.getServicios());
        destino.setTelefono(origen.getTelefono());
        destino.setEmail(origen.getEmail());
        destino.setFotografia(origen.getFotografia());
        destino.setIdAdministrador(origen.getIdAdministrador());
        destino.setEstado("ACTIVO");

        return destino;
    }

    private ClienteDTOPeticion fromBarberoUsuarioToCliente(ClienteUsuarioDTOPeticion origen) {
        if (origen == null) {
            return null;
        }

        ClienteDTOPeticion destino = new ClienteDTOPeticion();
        destino.setNombre(origen.getNombre());
        destino.setId(origen.getId());
        destino.setTelefono(origen.getTelefono());
        destino.setEmail(origen.getEmail());
        destino.setEstado("ACTIVO");

        return destino;
    }

}
