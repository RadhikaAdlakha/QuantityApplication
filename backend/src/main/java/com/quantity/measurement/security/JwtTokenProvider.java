package com.quantity.measurement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    // ── Create Token ──────────────────────────────────────────────────

    public String createToken(String email, String name, String picture) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .claim("picture", picture)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setIssuer("quantity-measurement-app")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Validate Token ────────────────────────────────────────────────

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("[JWT] Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("[JWT] Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("[JWT] Malformed token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("[JWT] Empty token: " + e.getMessage());
        }
        return false;
    }

    // ── Extract Claims ────────────────────────────────────────────────

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getNameFromToken(String token) {
        return (String) getClaims(token).get("name");
    }

    public long getExpirationMs() {
        return jwtExpirationMs;
    }

    // ── Private Helpers ───────────────────────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
