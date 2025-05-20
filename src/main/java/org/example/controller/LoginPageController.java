package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.*;
import org.example.security.JwtUtil;
import org.example.security.Role;
import org.example.security.UserPrincipal;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginPageController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final int cookieMaxAge;

    @Autowired
    public LoginPageController(UserService userService, AuthController authController, JwtUtil jwtUtil, AuthenticationManager authenticationManager, @Value("${cookie.max-age}") int cookieMaxAge) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.cookieMaxAge = cookieMaxAge;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new AuthRequest());
        return "login";
    }


    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute("loginRequest") AuthRequest request,
            Model model,
            HttpServletResponse response
    ) {
        try {
            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
            );

            // Генерация JWT токена
            String token = jwtUtil.generateToken(authentication);

            // Создание и настройка cookie с JWT
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(cookieMaxAge);
            response.addCookie(jwtCookie);

            // Получаем UserPrincipal для определения роли
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Role userRole = userPrincipal.getRole();

            // Перенаправляем в зависимости от роли
            return switch (userRole) {
                case ADMIN -> "redirect:/dashboard";
                case USER -> "redirect:/home";
            };

        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("user") RegisterRequest registerRequest,
            Model model
    ) {
        try {
            userService.createUser(
                    registerRequest.getLogin(),
                    registerRequest.getName(),
                    registerRequest.getPassword()
            );

            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            // Ошибка уникальности (например, логин уже существует)
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";
        } catch (IllegalArgumentException e) {
            // Ошибка валидации данных
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            // Общая ошибка
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }


}
