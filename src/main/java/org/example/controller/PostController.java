package org.example.controller;

import org.example.dto.PostDTO;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.service.PostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Посты", description = "Методы для работы с постами")
public class PostController {
    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Вывести все посты")
    @ApiResponse(responseCode = "200", description = "Список постов успешно получен")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }


    @PostMapping
    @Operation(summary = "Создать новый пост")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пост успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> createPost(@RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLogin = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentLogin);

        Post createdPost = postService.createPost(post.getContent(), currentUser.getId());
        URI location = URI.create("/posts/" + createdPost.getId());
        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/my/{id}")
    public ResponseEntity<Void> deleteMyPost(@PathVariable Long id) {
        // Достаем логин из токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Находим текущего пользователя
        User currentUser = userService.searchUserByLogin(currentUsername);

        // Находим пост по ID
        Post post = postService.getPostById(id);

        // Проверка: автор ли текущий пользователь
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        // Удаляем пост
        postService.deletePost(id);

        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
