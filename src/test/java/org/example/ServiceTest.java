package org.example;

import org.example.dto.PostDTO;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.entity.Subscription;
import org.example.service.PostService;
import org.example.service.SubscriptionService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.example.SocialNetworkApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SocialNetworkApplication.class)
@ExtendWith(SpringExtension.class)
public class ServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PostService postService;

    @Test
    public void a() {
        List<PostDTO> posts = subscriptionService.getPostsBySubscriberId(1);
        if (posts.isEmpty()) {
            System.out.println(" - Нет постов");
        } else {
            System.out.println("Посты автора:");
            for (PostDTO post : posts) {
                System.out.println(" - " + post.getContent());
            }
        }

    }
    @Test
    public void b(){
        userService.updateUserName(9, "Ванька-встанька");
    }
    @Test
    public void c(){
        userService.deleteUser(9);
    }

    @Test
    public void d(){
        subscriptionService.deleteSubscription(1,7);
    }

//    @Test
//    public void testGetSubscribedUsers() {
//        User user1 = userService.getUserById(1);
//
//        List<User> subscribedUsers = subscriptionService.findAuthorsBySubscriberId(1);
//
//        System.out.println("Пользователь " + user1.getLogin() + " подписан на:");
//
//        for (User author : subscribedUsers) {
//            System.out.println("ID: " + author.getId() + ", Имя: " + author.getLogin());
//
//            // Получаем все посты автора
//            List<Post> posts = postService.getPostsByAuthorId(author.getId());
//
//            if (posts.isEmpty()) {
//                System.out.println(" - Нет постов");
//            } else {
//                System.out.println("Посты автора:");
//                for (Post post : posts) {
//                    System.out.println(" - " + post.getContent());
//                }
//            }
//        }
//        System.out.println("Посты пользователя " + user1.getLogin());
//        List<Post> posts = postService.getPostsByAuthorId(user1.getId());
//        if (posts.isEmpty()) {
//            System.out.println(" - Нет постов");
//        } else {
//            for (Post post : posts) {
//                System.out.println(" - " + post.getContent());
//            }
//        }
//
//    }
}
