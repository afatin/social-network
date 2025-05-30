package org.example.controller;


import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.service.RevokedTokenService;
import org.example.service.SubscriptionService;
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
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final SubscriptionService subscriptionService;
    private final RevokedTokenService revokedTokenService;

    public UserController(UserService userService, PostService postService, SubscriptionService subscriptionService, RevokedTokenService revokedTokenService) {
        this.userService = userService;
        this.postService = postService;
        this.subscriptionService = subscriptionService;
        this.revokedTokenService = revokedTokenService;
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(
                user.getLogin(),
                user.getName(),
                user.getPassword()
        );

        URI location = URI.create("/users/" + createdUser.getId());  // генерируем URI нового ресурса
        return ResponseEntity.created(location).build(); // возвращаем статус 201 и Location заголовок
    }
    @GetMapping
    @Operation(summary = "Вывести всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id
    ) {
        UserDTO userDTO = userService.getUserById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("{name}/search")
    @Operation(summary = "Найти пользователя по никнейму")
    @ApiResponse(responseCode = "200", description = "Результаты поиска успешно получены")
    public ResponseEntity<List<UserDTO>> searchUserByName(
            @Parameter(description = "Никнейм пользователя", required = true) @PathVariable String name
    ) {
        return ResponseEntity.ok(userService.searchUserByName(name));
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Получить все посты пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<PostDTO>> getUserPosts(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id
    ) {
        UserDTO userDTO = userService.getUserById(id);
        return userDTO != null ? ResponseEntity.ok(postService.getPostsByAuthorId(id)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/all_posts")
    @Operation(summary = "Посты авторов, на которых подписан пользователь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<PostDTO>> getPostsBySubscriberId(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id
    ) {
        UserDTO userDTO = userService.getUserById(id);
        return userDTO != null ? ResponseEntity.ok(subscriptionService.getPostsBySubscriberId(id)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentUsername);
        userService.deleteUser(currentUser.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/password")
    @Operation(summary = "Изменить пароль текущего пользователя")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authHeader, @RequestBody String newPassword) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentUsername);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        userService.updateUserPassword(currentUser.getId(), newPassword);

        // Отзываем текущий токен после смены пароля
        String token = authHeader.substring(7); // Убираем "Bearer "
        revokedTokenService.revokeToken(token);

        return ResponseEntity.ok("Password updated successfully. Please login again.");
    }

}
