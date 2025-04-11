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
            @ApiResponse(responseCode = "200", description = "Подписка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> createSubscription(@RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createSubscription(
                subscription.getSubscriber().getId(),
                subscription.getAuthor().getId()
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
    public ResponseEntity<Void> deleteSubscription(
            @RequestParam Integer subscriberId,
            @RequestParam Integer authorId
    ) {
        subscriptionService.deleteSubscription(subscriberId, authorId);
        return ResponseEntity.ok().build();
    }


}
