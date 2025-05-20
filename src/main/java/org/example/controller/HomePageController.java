package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.RevokedTokenService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
public class HomePageController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PostService postService;
    private final RevokedTokenService revokedTokenService;

    @Autowired
    public HomePageController(JwtUtil jwtUtil, UserService userService, PostService postService,
                              RevokedTokenService revokedTokenService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.postService = postService;
        this.revokedTokenService = revokedTokenService;
    }

    @GetMapping("/home")
    public String showHomePage(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            Model model
    ) {


        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        Long userId = jwtUtil.getUserIdFromToken(jwt);
        UserDTO userDTO = userService.getUserById(userId);
        List<PostDTO> userPosts = postService.getPostsByAuthorId(userId);


        model.addAttribute("name", userDTO.getName());
        model.addAttribute("posts", userPosts);
        return "home";
    }

    @PostMapping("/home/new-post")
    public String createNewPost(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            @RequestParam("content") String content
    ) {
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        Long userId = jwtUtil.getUserIdFromToken(jwt);

        postService.createPost(content, userId);

        return "redirect:/home";
    }

    @PostMapping("/home/delete-post")
    public String deletePost(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            @RequestParam("postId") Long postId
    ) {
        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        postService.deletePost(postId);

        return "redirect:/home";
    }

    @PostMapping("/password")
    public String updatePassword(
            @CookieValue(value = "jwt", defaultValue = "") String jwt,
            @RequestParam("newPassword") String newPassword
    ) {

        Long userId = jwtUtil.getUserIdFromToken(jwt);

        userService.updateUserPassword(userId, newPassword);

        revokedTokenService.revokeToken(jwt);

        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }

    @PostMapping("/delete-account-confirm")
    public String confirmDeleteAccount(Model model) {
        model.addAttribute("showConfirmation", true);
        return "settings";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@CookieValue(value = "jwt", defaultValue = "") String jwt) {

        Long userId = jwtUtil.getUserIdFromToken(jwt);

        userService.deleteUser(userId);
        revokedTokenService.revokeToken(jwt);

        return "redirect:/login";
    }

    @PostMapping("/out")
    public String logout(@CookieValue(value = "jwt", defaultValue = "") String jwt,
                         HttpServletResponse response) {

        revokedTokenService.revokeToken(jwt);

        return "redirect:/login";
    }



}
