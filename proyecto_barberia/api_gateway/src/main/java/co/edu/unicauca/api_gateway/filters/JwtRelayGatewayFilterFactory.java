package co.edu.unicauca.api_gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;

// Componente de Spring que implementa un filtro de Gateway para validar JWT
@Component
public class JwtRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtRelayGatewayFilterFactory.Config> {

    // Validador de tokens JWT inyectado por Spring
    @Autowired
    private JwtValidator jwtValidator;

    // Cliente WebClient para comunicarse con el microservicio de identidad
    @Autowired
    private WebClient identidadServiceClient;

    // Constructor que indica la clase de configuración del filtro
    public JwtRelayGatewayFilterFactory() {
        super(Config.class);
    }

    public static class Config {
    }

    // Aplica el filtro a cada request interceptado por el Gateway
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath(); // Obtiene la ruta de la request.
            String method = exchange.getRequest().getMethod().name(); // Obtiene el método HTTP.

            System.out.println("Procesando: " + method + " " + path); // Log de depuración.

            if (isPublicPath(path)) { // Omite paths públicos.
                return chain.filter(exchange);
            }

            String token = extractToken(exchange.getRequest()); // Extrae el JWT del header.
            if (token == null || !jwtValidator.validate(token)) { // Valida el token.
                return unauthorized(exchange, "JWT inválido o faltante");
            }

            String username = jwtValidator.getUsernameFromToken(token); // Obtiene el username del token.

            // Llama al microservicio de identidad para validar permisos.
            return identidadServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/auth/validate-access")
                            .queryParam("path", path)
                            .queryParam("method", method)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("Error en servicio de autenticación: " + response.statusCode())))
                    .bodyToMono(ValidationResponse.class)
                    .flatMap(validation -> {
                        if (validation.authorized()) { // Permisos permitidos.
                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Id", username)
                                    .header("X-User-Roles", String.join(",", validation.roles()))
                                    .header("X-User-Name", username)
                                    .build();
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        } else { // Permisos insuficientes.
                            return forbidden(exchange, "Permisos insuficientes para " + method + " " + path);
                        }
                    })
                    .onErrorResume(e -> { // Manejo de error si el servicio de autenticación falla.
                        System.err.println("Servicio de autenticación no disponible: " + e.getMessage());
                        return unauthorized(exchange, "Servicio de autorización no disponible");
                    });
        };
    }


    // Extrae el token JWT del header Authorization de la request
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Retorna el token quitando el prefijo "Bearer"
            return authHeader.substring(7);
        }
        // Retorna null si no se encontró un token válido.
        return null;
    }

    // Determina si la ruta es pública y no requiere autenticación
    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth/") ||
                path.equals("/api/usuario/registro");
    }

    // Devuelve una respuesta 401 Unauthorized en formato JSON
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String responseBody = "{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // Devuelve una respuesta 403 Forbidden en formato JSON
    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String responseBody = "{\"error\": \"Forbidden\", \"message\": \"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // Representa la respuesta del microservicio de validación de acceso
    public record ValidationResponse(boolean authorized, java.util.List<String> roles) {}
}