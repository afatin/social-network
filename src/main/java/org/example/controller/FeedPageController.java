package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.RevokedTokenService;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class FeedPageController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    private final SubscriptionService subscriptionService;

    @Autowired
    public FeedPageController(JwtUtil jwtUtil, UserService userService,
                              SubscriptionService subscriptionService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/feed")
    public String getFeedPage(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            Model model
    ) {
        System.out.println("Token from request: " + jwt);
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }


        Long userId = jwtUtil.getUserIdFromToken(jwt);
        UserDTO userDTO = userService.getUserById(userId);
        List<PostDTO> posts = subscriptionService.getPostsBySubscriberId(userId);

        if (userDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("posts", posts);

        return "feed";
    }

}
