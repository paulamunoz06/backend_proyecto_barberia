package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.services;

import java.util.List;
import java.util.stream.Collectors;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.ValidacionDTORespuesta;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.models.MicroservicioPermission;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.BadCredentialException;
import co.edu.unicauca.microservicio_identidad_acceso.Excepciones.excepcionesPropias.ValidacionException;
import co.edu.unicauca.microservicio_identidad_acceso.Seguridad.jwt.JwtUtils;
import co.edu.unicauca.microservicio_identidad_acceso.Seguridad.services.UserDetailsImpl;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.accesoADatos.PermissionRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.LoginDTOSolicitud;
import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.fachada.DTOs.LoginDTORespuesta;

@Service
public class AutenticacionServiceImpl implements IAutenticacionService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public LoginDTORespuesta authenticateUser(LoginDTOSolicitud loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return new LoginDTORespuesta(jwt, userDetails.getUsername());

        } catch (BadCredentialsException ex) {
            throw new BadCredentialException("Credenciales inválidas: usuario o contraseña incorrectos.");
        } catch (Exception ex) {
            throw new RuntimeException("Error al autenticar el usuario: " + ex.getMessage());
        }
    }

    @Override
    public ValidacionDTORespuesta validarAcceso(String path, String method, UserDetailsImpl userDetails) {
        try{
            // Obtener los permisos para la ruta y metodo
            List<MicroservicioPermission> permissions = permissionRepository.findPermissionsForPath(path, method);

            if (permissions.isEmpty()) {
                throw new ValidacionException("No hay permisos definidos para el metodo: " + method + " " + path);
            }
            System.out.println(permissions);boolean authorized =true;

            // Verificar si el usuario tiene alguno de los roles requeridos
            authorized = permissions.stream()
                    .anyMatch(permission -> {
                        List<String> requiredRoles = permission.getRequiredRolesAsList();
                        return userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .anyMatch(userRole -> requiredRoles.contains(userRole));
                    });

            if (!authorized) {
                System.out.println("User " + userDetails.getUsername() +
                        " with roles " + userDetails.getAuthorities() +
                        " denied access to " + method + " " + path);
            }

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new ValidacionDTORespuesta(authorized,roles);
        } catch (Exception e) {
            throw new ValidacionException("Error validando el acceso para la ruta: " + path + " - " + e.getMessage());
        }
    }
}
