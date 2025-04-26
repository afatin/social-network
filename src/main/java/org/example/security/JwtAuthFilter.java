package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.User;
import org.example.service.RevokedTokenService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

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

        // Пропускаем проверку для эндпоинтов авторизации
        if (request.getRequestURI().startsWith("/auth/register") ||
                request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/auth/ping"))  {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        // Если токен отсутствует, возвращаем 401
        if (token == null) {
            sendErrorResponse(response, "Unauthorized", "Authorization token is missing.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Если токен есть, проверяем его валидность
        if (jwtUtil.validateToken(token)) {
            if (revokedTokenService.isTokenRevoked(token)) {
                sendErrorResponse(response, "Unauthorized", "Token has been revoked.", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Authentication authentication = jwtUtil.getAuthentication(token);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            return token;
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String error, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Подготовка объекта для вывода
        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", error);
        responseBody.put("message", message);
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", status);

        // Преобразование в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(responseBody);

        // Отправка ответа
        response.getWriter().write(json);
    }
}