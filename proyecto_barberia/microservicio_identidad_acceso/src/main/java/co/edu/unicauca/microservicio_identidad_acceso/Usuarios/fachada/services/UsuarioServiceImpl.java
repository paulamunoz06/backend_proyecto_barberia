package co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.services;

import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.BarberoClient;
import co.edu.unicauca.microservicio_identidad_acceso.Comunicacion.REST.ClienteClient;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.EntidadYaExisteException;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.ReglaNegocioExcepcion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos.RoleRepository;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.accesoADatos.UserRepository;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.BarberoUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.fachada.DTOs.ClienteUsuarioDTOPeticion;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.ERole;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.Role;
import co.edu.unicauca.microservicio_identidad_acceso.Usuarios.modelos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

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

    @Override
    public ResponseEntity<?> registerBarbero(BarberoUsuarioDTOPeticion signUpRequest, MultipartFile archivo) {
        if(userRepository.existsByUsername(signUpRequest.getId())) {
            throw new EntidadYaExisteException("El usuario ya se encuentra registrado");
        }

        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.BARBERO).orElseThrow(() -> new ReglaNegocioExcepcion("El rol no existe"));
        roles.add(modRole);

        BarberoDTOPeticion prelim = fromBarberoUsuarioToBarbero(signUpRequest);
        ResponseEntity<String> resp = barberoClient.crearPreliminar(prelim);

        if (resp.getStatusCode().is2xxSuccessful()) {
            User user = new User(signUpRequest.getId(),encoder.encode(signUpRequest.getPassword()));
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.status(201).body("Usuario y barbero creados");
        } else {
            return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
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
