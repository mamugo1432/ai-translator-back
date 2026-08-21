package es.ia.translator.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET = "mi_clave_super_secreta_para_jwt_que_debe_ser_larga";

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    // Generar token
    public String generateToken(String username, String email) {

        return Jwts.builder()
                .subject(username)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // Extraer username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Validar token
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Obtener clave de firma
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Conseguir Claims del token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extraer rol
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }
}