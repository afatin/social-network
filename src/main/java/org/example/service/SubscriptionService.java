package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.example.dto.DTOConverter;
import org.example.dto.PostDTO;
import org.example.entity.Post;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubscriptionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DTOConverter dtoConverter;

    @Transactional
    public Subscription createSubscription(Long subscriberId, Long authorId){
        User subscriber = entityManager.find(User.class, subscriberId);
        User author = entityManager.find(User.class, authorId);

        Subscription subscription = new Subscription();
        subscription.setAuthor(author);
        subscription.setSubscriber(subscriber);
        entityManager.persist(subscription);
        return subscription;
    }

    @Transactional
    public void deleteSubscription(Long subscriberId, Long authorId) {
        TypedQuery<Subscription> query = entityManager.createQuery(
                "SELECT s FROM Subscription s WHERE s.subscriber.id = :subscriberId AND s.author.id = :authorId",
                Subscription.class
        );
        query.setParameter("subscriberId", subscriberId);
        query.setParameter("authorId", authorId);

        List<Subscription> subscriptions = query.getResultList();

        for (Subscription subscription : subscriptions) {
            entityManager.remove(subscription);
        }
    }

    public List<User> findAuthorsBySubscriberId(Long subscriberId) {
        User subscriber = entityManager.find(User.class, subscriberId);
        String query = "SELECT s.author FROM Subscription s WHERE s.subscriber = :subscriber";
        return entityManager.createQuery(query, User.class)
                .setParameter("subscriber", subscriber)
                .getResultList();
    }

    public List<PostDTO> getPostsBySubscriberId(Long subscriberId) {
        List<User> authors = findAuthorsBySubscriberId(subscriberId);
        String query = "SELECT p FROM Post p WHERE p.author IN :authors";
        List<Post> posts = entityManager.createQuery(query, Post.class)
                .setParameter("authors", authors)
                .getResultList();

        return posts.stream()
                .map(dtoConverter::convertToPostDTO)
                .toList();
    }

}
