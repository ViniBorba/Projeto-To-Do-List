package com.example.demo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;
   
    private static final long ALLOWED_SKEW_SECONDS = 30;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expirationMillis}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("typ", "access"))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
       
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(ALLOWED_SKEW_SECONDS)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

// JwtService
public Optional<String> validateAndGetUsername(String token) {
    try {
        Claims c = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(30) // tolerancia de clock
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date exp = c.getExpiration();
        if (exp == null || exp.before(new Date())) return Optional.empty();
        return Optional.ofNullable(c.getSubject());
    } catch (Exception e) {
        return Optional.empty();
    }
}

public boolean isValid(String token) {
    return validateAndGetUsername(token).isPresent();
}




    
}
