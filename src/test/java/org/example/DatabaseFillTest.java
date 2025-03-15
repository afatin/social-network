package org.example;

import org.example.entity.Post;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.example.service.PostService;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.example.SocialNetworkApplication;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SocialNetworkApplication.class)
@ExtendWith(SpringExtension.class)
public class DatabaseFillTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFillDatabase() {
        // Создание пользователей
        User user1 = userService.createUser("nastya", "Настя", "123");
        User user2 = userService.createUser("artem", "Артём", "123");
        User user3 = userService.createUser("oleg", "Олег", "123");
        User user4 = userService.createUser("vika", "Вика", "123");

        Post post1 = postService.createPost("всем привет, я Настя!", user1);
        Post post2 = postService.createPost("всем привет, я Артём!", user2);
        Post post3 = postService.createPost("всем привет, я Олег!", user3);
        Post post4 = postService.createPost("всем привет, я Вика!", user4);

        Subscription subs1 = subscriptionService.createSubscription(user1, user2);
        Subscription subs2 = subscriptionService.createSubscription(user2, user1);
        Subscription subs3 = subscriptionService.createSubscription(user1, user3);
        Subscription subs4 = subscriptionService.createSubscription(user1, user4);
        Subscription subs5 = subscriptionService.createSubscription(user4, user1);


        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
        assertNotNull(user3.getId());
        assertNotNull(user4.getId());

        assertNotNull(post1.getId());
        assertNotNull(post2.getId());
        assertNotNull(post3.getId());
        assertNotNull(post4.getId());

        assertNotNull(subs1.getId());
        assertNotNull(subs2.getId());
        assertNotNull(subs3.getId());
        assertNotNull(subs4.getId());
        assertNotNull(subs5.getId());
    }
}
