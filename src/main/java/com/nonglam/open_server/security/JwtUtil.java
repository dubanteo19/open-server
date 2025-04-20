package com.nonglam.open_server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key accessTokenKey;
    private final Key refreshTokenKey;
    private final long accessTokenExpirationMs = 1000L * 60 * 15;
    private final long refreshTokenExpirationMs = 1000L * 60 * 60 * 24 * 30;

    public JwtUtil(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.refresh.secret}") String refreshSecret
    ) {
        this.accessTokenKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        return generateToken(username, claims, accessTokenExpirationMs, accessTokenKey);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, Map.of(), refreshTokenExpirationMs, refreshTokenKey);
    }

    public Claims getClaims(String token, boolean isRefresh) {
        var re = Jwts.parserBuilder()
                .setSigningKey(isRefresh ? refreshTokenKey : accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return re;
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver, boolean isRefresh) {
        var claims = getClaims(token, isRefresh);
        return resolver.apply(claims);
    }

    public String extractUsername(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getSubject, isRefresh);
    }

    public boolean isTokenValid(String token, boolean isRefresh) {
        try {
            getClaims(token, isRefresh);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(String subject, Map<String, Object> claims,
                                long expirationMs, Key key) {
        var now = new Date();
        var expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
