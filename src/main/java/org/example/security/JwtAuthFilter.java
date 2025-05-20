package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.RevokedTokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RevokedTokenService revokedTokenService;

    public JwtAuthFilter(JwtUtil jwtUtil, RevokedTokenService revokedTokenService) {
        this.jwtUtil = jwtUtil;
        this.revokedTokenService = revokedTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Пропускаем проверку для открытых эндпоинтов
        String path = request.getRequestURI();
        if (path.startsWith("/login") ||
                path.startsWith("/register") ||
                path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new BadCredentialsException("Authorization token is missing.");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new BadCredentialsException("Invalid JWT token.");
        }

        if (revokedTokenService.isTokenRevoked(token)) {
            throw new BadCredentialsException("Token has been revoked.");
        }

        Authentication authentication = jwtUtil.getAuthentication(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
