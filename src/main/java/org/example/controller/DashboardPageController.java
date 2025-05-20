package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.security.JwtUtil;
import org.example.service.PostService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardPageController {

        private final UserService userService;
        private final PostService postService;
        private final JwtUtil jwtUtil;

        @Autowired
        public DashboardPageController(UserService userService,
                                   PostService postService,
                                   JwtUtil jwtUtil) {
            this.userService = userService;
            this.postService = postService;
            this.jwtUtil = jwtUtil;
        }

        @GetMapping("/dashboard")
        public String dashboard(@CookieValue(value = "jwt", defaultValue = "") String jwt,
                                Model model) {

            Long currentUserId = jwtUtil.getUserIdFromToken(jwt);
            UserDTO currentUser = userService.getUserById(currentUserId);


            List<UserDTO> allUsers = userService.getAllUsers().stream()
                    .filter(user -> !user.getId().equals(currentUserId))
                    .collect(Collectors.toList());

            model.addAttribute("users", allUsers);
            return "dashboard";
        }

        @PostMapping("/dashboard/delete-user/{userId}")
        public String deleteUser(@PathVariable Long userId,
                                 @CookieValue(value = "jwt") String jwt) {

            if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
                return "redirect:/login";
            }

            userService.deleteUser(userId);

            return "redirect:/dashboard";
        }

    @GetMapping("/dashboard/user-posts/{userId}")
    public String viewUserPosts(@PathVariable Long userId,
                                @CookieValue(value = "jwt") String jwt,
                                Model model) {

        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        List<PostDTO> userPosts = postService.getPostsByAuthorId(userId);
        UserDTO user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        return "user-posts";
    }
    @PostMapping("/dashboard/user-posts/delete-post")
    public String deletePost(@RequestParam("postId") Long postId,
                             @CookieValue(value = "jwt") String jwt,
                             @RequestParam(value = "userId") Long userId) {

        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            return "redirect:/login";
        }

        postService.deletePost(postId);

        return "redirect:/dashboard/user-posts/" + userId;
    }
}

