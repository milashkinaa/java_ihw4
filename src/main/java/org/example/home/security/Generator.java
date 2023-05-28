package org.example.home.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class Generator {

    public Date expireDate;
    public static final long JWT_EXPIRATION = 70000;
    public static final String JWT_SECRET = "32785627897218149YGSFUGASHFLASYDUGFSYUDLGFULSKDGFSLDHJUGLF01925237598237";
    public String generateToken(String subject) {
        byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        expireDate = new Date(new Date().getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }
}
