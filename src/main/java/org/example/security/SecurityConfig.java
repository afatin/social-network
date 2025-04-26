package org.example.security;

import org.example.service.MyUserDetailsService;
import org.example.service.RevokedTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RevokedTokenService revokedTokenService;

    public SecurityConfig(JwtUtil jwtUtil, MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, RevokedTokenService revokedTokenService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.revokedTokenService = revokedTokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/auth/register", "/auth/login", "/auth/ping").permitAll()
                                .requestMatchers("/auth/logout").authenticated()

                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/me").hasRole("USER")
                                .requestMatchers( HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
                                .requestMatchers( HttpMethod.POST,"/api/users/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/posts/my/**").hasRole("USER")
                                .requestMatchers( HttpMethod.DELETE,"/api/posts/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/subscriptions/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/subscriptions/**").hasRole("USER")
                )

                .addFilterBefore(new JwtAuthFilter(jwtUtil, revokedTokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}