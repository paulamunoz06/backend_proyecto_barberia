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
    public boolean validate(String authToken) {
        try {
            System.out.println("Validando token JWT: " + authToken);
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            return false;
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