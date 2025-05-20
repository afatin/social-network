package org.example.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping("/401")
    public String error401() {
        return "error/401";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }

    @GetMapping("/404")
    public String error404() {
        return "error/404";
    }

    @RequestMapping
    public String handleError(HttpServletResponse response) {
        int status = response.getStatus();
        if (status == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        } else if (status == HttpStatus.UNAUTHORIZED.value()) {
            return "error/401";
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        }
        return "error/404"; // fallback
    }
}
