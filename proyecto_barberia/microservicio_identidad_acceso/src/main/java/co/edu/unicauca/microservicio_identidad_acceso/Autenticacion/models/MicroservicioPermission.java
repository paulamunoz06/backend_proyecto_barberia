package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "microservicio_permissions")
public class MicroservicioPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "microservice_name")
    private String microserviceName;

    @Column(name = "endpoint_pattern")
    private String endpointPattern;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "required_roles")
    private String requiredRoles;

    private String description;

    public List<String> getRequiredRolesAsList() {
        if (requiredRoles == null || requiredRoles.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(requiredRoles.split(","));
    }

    public void setRequiredRolesFromList(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            this.requiredRoles = null;
        } else {
            this.requiredRoles = String.join(",", roles);
        }
    }
}