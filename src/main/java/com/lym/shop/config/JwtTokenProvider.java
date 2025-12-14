package com.lym.shop.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "a2V5LWZvci1zaG9wLXNwcmluZy1qc3QtMTIzNDU2Nzg5MDEyMzQ1Ng=="; // Base64
    private static final long ACCESS_TOKEN_EXPIRE_MILLIS  = 1000L * 60 * 60;       // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_MILLIS = 1000L * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    public JwtTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_MILLIS, "access");
    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_MILLIS, "refresh");
    }

    private String generateToken(Authentication authentication, long expireMillis, String type) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .claim("type", type)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 username 꺼내기
    public String getUsername(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // 토큰 타입(access/refresh) 확인하고 싶을 때
    public String getTokenType(String token) {
        Object type = parseClaims(token).getBody().get("type");
        return type != null ? type.toString() : null;
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException |
                 ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
