package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    @GetMapping("/login")
    public String withSession(HttpServletRequest request) {
        HttpSession session = request.getSession(); // Создает сессию

        session.setAttribute("user", "Alice");

        return "Session created";
    }
}
