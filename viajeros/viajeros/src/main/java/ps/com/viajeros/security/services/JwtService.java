package ps.com.viajeros.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }
    public String extraerUsername(String token){
        //return extraerPartesToken(token,Claims::getSubject);
        return extraerContenidoClaims(token).getSubject();
    }

    public Claims extraerContenidoClaims(String token){
        // parser: convierte a String, establece la clave para determinar si el JWT es valido dentro del header
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    } //
}
