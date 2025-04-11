package org.example.dto;

import org.example.entity.Post;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class DTOConverter {

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getLogin());
    }

    public PostDTO convertToPostDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getContent(),
                post.getAuthor().getId(),
                post.getAuthor().getName()
        );
    }

    public static SubscriptionDTO convertToSubscriptionDTO(Subscription subscription) {
        if (subscription == null || subscription.getSubscriber() == null || subscription.getAuthor() == null) {
            return null;
        }
        return new SubscriptionDTO(
                (Integer) subscription.getId(),
                subscription.getSubscriber().getId(),
                subscription.getSubscriber().getName(),
                subscription.getAuthor().getId(),
                subscription.getAuthor().getName()
        );
    }
}
