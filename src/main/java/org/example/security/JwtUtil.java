package org.example.security;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {
        private final Key key;
        private final long jwtExpiration;

        public JwtUtil(
                @Value("${jwt.secret}") String secret,
                @Value("${jwt.expiration}") long jwtExpiration
        ) {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
            this.jwtExpiration = jwtExpiration;
        }

        // Генерация токена
        public String generateToken(Authentication authentication) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            String role = userPrincipal.getRole().name();

            return Jwts.builder()
                    .setSubject(userPrincipal.getUsername())                  // login
                    .claim("id", userPrincipal.getId())                    // id
                    .claim("role", role)                                   // роль
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(key)
                    .compact();
        }

        // Валидация токена
        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        }

        // Получить аутентификацию по токену
        public Authentication getAuthentication(String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = claims.get("id", Long.class);
            String login = claims.getSubject();
            String roleStr = claims.get("role", String.class);
            Role role = Role.valueOf(roleStr);

            UserPrincipal userPrincipal = new UserPrincipal(id, login, null, role);

            Collection<SimpleGrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_" + roleStr));


            return new UsernamePasswordAuthenticationToken(userPrincipal, "", authorities);
        }

        // Получить логин из токена
        public String getLoginFromToken(String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", Integer.class).longValue();
    }
    }
