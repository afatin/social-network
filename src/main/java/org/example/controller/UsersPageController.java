package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UsersPageController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    @Autowired
    public UsersPageController(JwtUtil jwtUtil, UserService userService, PostService postService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/users")
    public String getUsersPage(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            Model model
    ) {
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        Long userId = jwtUtil.getUserIdFromToken(jwt);
        UserDTO currentUser = userService.getUserById(userId);

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<UserDTO> allUsers = userService.getAllUsers();
        List<User> subscribed = subscriptionService.findAuthorsBySubscriberId(userId);

        Set<Long> subscribedIds = subscribed.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        List<UserDTO> subscribedUsers = new ArrayList<>();
        List<UserDTO> otherUsers = new ArrayList<>();

        for (UserDTO user : allUsers) {
            if ((user.getId().equals(userId)) || (Objects.equals(user.getLogin(), "admin"))) {
                continue;
            }
            if (subscribedIds.contains(user.getId())) {
                subscribedUsers.add(user);
            } else {
                otherUsers.add(user);
            }
        }

        model.addAttribute("subscribedUsers", subscribedUsers);
        model.addAttribute("otherUsers", otherUsers);
        model.addAttribute("currentUserId", userId); // Для подписки/отписки

        return "users";
    }

    @PostMapping("/subscribe")
    public String subscribe(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            @RequestParam Long authorId
    ) {
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        Long subscriberId = jwtUtil.getUserIdFromToken(jwt);
        subscriptionService.createSubscription(subscriberId, authorId);
        return "redirect:/users";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            @RequestParam Long authorId
    ) {
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        Long subscriberId = jwtUtil.getUserIdFromToken(jwt);
        subscriptionService.deleteSubscription(subscriberId, authorId);
        return "redirect:/users";
    }

}
