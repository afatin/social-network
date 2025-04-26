package org.example;

import org.example.entity.Post;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.security.Role;
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
        User user1 = userService.createUser("admin", "admin", "admin", Role.ADMIN);


//        Post post1 = postService.createPost("всем привет, я Настя!", 1);
//        Post post2 = postService.createPost("всем привет, я Артём!", 2);
//        Post post3 = postService.createPost("всем привет, я Олег!", 3);
//        Post post4 = postService.createPost("всем привет, я Вика!", 4);

//        Subscription subs1 = subscriptionService.createSubscription(1, 2);
//        Subscription subs2 = subscriptionService.createSubscription(2, 1);
//        Subscription subs3 = subscriptionService.createSubscription(1, 3);
//        Subscription subs4 = subscriptionService.createSubscription(1, 4);
//        Subscription subs5 = subscriptionService.createSubscription(4, 1);


        assertNotNull(user1.getId());
//        assertNotNull(user2.getId());
//        assertNotNull(user3.getId());
//        assertNotNull(user4.getId());
//
//        assertNotNull(post1.getId());
//        assertNotNull(post2.getId());
//        assertNotNull(post3.getId());
//        assertNotNull(post4.getId());
//
//        assertNotNull(subs1.getId());
//        assertNotNull(subs2.getId());
//        assertNotNull(subs3.getId());
//        assertNotNull(subs4.getId());
//        assertNotNull(subs5.getId());
    }
}
