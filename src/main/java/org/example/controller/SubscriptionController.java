package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.service.PostService;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Подписки", description = "Методы для работы с подписками")
public class SubscriptionController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public SubscriptionController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @Operation(summary = "Создать новую подписку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Подписка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> createSubscription(@RequestBody Subscription subscription) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentUsername);

        Subscription createdSubscription = subscriptionService.createSubscription(
                currentUser.getId(),                     // берем подписчика из токена
                subscription.getAuthor().getId()         // автора берем из тела запроса
        );

        URI location = URI.create("/subscriptions/" + createdSubscription.getId());
        return ResponseEntity.created(location).build();
    }


    @DeleteMapping
    @Operation(summary = "Удалить подписку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Подписка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> deleteSubscription(@RequestBody Subscription request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userService.searchUserByLogin(currentUsername);

        subscriptionService.deleteSubscription(currentUser.getId(), request.getAuthor().getId());
        return ResponseEntity.ok().build();
    }



}
