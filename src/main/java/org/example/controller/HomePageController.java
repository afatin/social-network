package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.RevokedTokenService;
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
public class HomePageController {

    private final AuthController authController;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PostService postService;
    private final RevokedTokenService revokedTokenService;

    @Autowired
    public HomePageController(AuthController authController, JwtUtil jwtUtil, UserService userService, PostService postService,
                              RevokedTokenService revokedTokenService) {
        this.authController = authController;
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

        if (userDTO == null) {
            return "redirect:/login";
        }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentUsername);

        userService.updateUserPassword(currentUser.getId(), newPassword);

        revokedTokenService.revokeToken(jwt);

        return "redirect:/login";
    }



}
