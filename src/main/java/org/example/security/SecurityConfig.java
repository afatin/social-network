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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RevokedTokenService revokedTokenService;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtUtil jwtUtil,
                          MyUserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          RevokedTokenService revokedTokenService,
                          AuthenticationEntryPoint customAuthenticationEntryPoint,
                          AccessDeniedHandler customAccessDeniedHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.revokedTokenService = revokedTokenService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())



                .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/api/auth/register", "/api/auth/login", "/login", "/register", "/error/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/out").authenticated()

                                .requestMatchers( "/home/**").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/password").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/settings").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/delete-account-confirm").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/delete-account").hasRole("USER")


                                .requestMatchers(HttpMethod.GET, "/users").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/subscribe").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/unsubscribe").hasRole("USER")

                                .requestMatchers(HttpMethod.GET, "/feed").hasRole("USER")

                                .requestMatchers("/dashboard/**").hasRole("ADMIN")


                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/me").hasRole("USER")
                                .requestMatchers( HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
                                .requestMatchers( HttpMethod.POST,"/api/users/**").hasAnyRole("USER", "ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/posts/my/**").hasRole("USER")
                                .requestMatchers( HttpMethod.DELETE,"/api/posts/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/subscriptions/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/subscriptions/**").hasRole("USER")
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
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