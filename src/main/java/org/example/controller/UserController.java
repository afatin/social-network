package org.example.controller;


import org.example.entity.Post;
import org.example.entity.User;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.example.service.PostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final SubscriptionService subscriptionService;

    public UserController(UserService userService, PostService postService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.postService = postService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(
                user.getLogin(),
                user.getName(),
                user.getPassword()
        ));
    }
    @GetMapping
    @Operation(summary = "Вывести всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Integer id
    ) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Найти пользователя по никнейму")
    @ApiResponse(responseCode = "200", description = "Результаты поиска успешно получены")
    public ResponseEntity<List<User>> searchUserByName(
            @Parameter(description = "Никнейм пользователя", required = true) @RequestParam String name
    ) {
        return ResponseEntity.ok(userService.searchUserByName(name));
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Получить все посты пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Post>> getUserPosts(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Integer id
    ) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(postService.getPostsByAuthorId(id)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/all_posts")
    @Operation(summary = "Посты авторов, на которых подписан пользователь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Post>> getPostsBySubscriberId(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Integer id
    ) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(subscriptionService.getPostsBySubscriberId(id)) : ResponseEntity.notFound().build();
    }

}
