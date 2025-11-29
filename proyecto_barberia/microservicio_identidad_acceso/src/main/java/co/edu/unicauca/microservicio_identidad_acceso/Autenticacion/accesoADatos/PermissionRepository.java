package co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.accesoADatos;

import co.edu.unicauca.microservicio_identidad_acceso.Autenticacion.models.MicroservicioPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PermissionRepository extends JpaRepository<MicroservicioPermission, Long> {

    @Query(value = "SELECT * FROM microservicio_permissions p WHERE " +
            "(:path LIKE REPLACE(p.endpoint_pattern, '*', '%')) AND " +
            "(p.http_method = '*' OR p.http_method = :method)",
            nativeQuery = true)
    List<MicroservicioPermission> findPermissionsForPath(String path, String method);
}
