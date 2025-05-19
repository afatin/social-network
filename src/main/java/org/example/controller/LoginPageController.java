package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.*;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginPageController {

    private final AuthController authController;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LoginPageController(AuthController authController, JwtUtil jwtUtil, UserService userService, PostService postService) {
        this.authController = authController;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.postService = postService;
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
            ResponseEntity<AuthResponse> authResponse = authController.login(request);

            String jwtToken = authResponse.getBody().getToken();

            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 1 день
            response.addCookie(jwtCookie);

            return "redirect:/home";
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
            authController.registerUser(registerRequest);

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }


}
