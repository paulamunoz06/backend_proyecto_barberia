package co.edu.unicauca.api_gateway.filters;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

// Componente de Spring que valida y extrae información de tokens JWT
@Component
public class JwtValidator {

    // Se inyecta la clave secreta del JWT desde application.properties
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Genera la clave secreta en formato Key para verificar JWT
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Valida que el token JWT sea correcto y esté firmado con la clave esperada
    public void validate(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .setAllowedClockSkewSeconds(60) // evita errores por desfase entre microservicios
                    .build()
                    .parseClaimsJws(authToken);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("El token ha expirado.");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new RuntimeException("El token tiene un formato no soportado.");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new RuntimeException("El token está mal formado.");
        } catch (SignatureException e) {
            throw new RuntimeException("La firma del token no es válida.");
        } catch (Exception e) {
            throw new RuntimeException("Token inválido: " + e.getMessage());
        }
    }

    // Extrae el nombre de usuario del token JWT
    public String getUsernameFromToken(String token) {
        try {
            String username = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
            return username;
        } catch (Exception ex) {
            System.err.println("Error extrayendo el nombre de usuario desde el token: " + ex.getMessage());
            return "desconocido";
        }
    }
}