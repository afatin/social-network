package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public Subscription createSubscription(User subscriber, User author){
        Subscription subscription = new Subscription();
        subscription.setAuthor(author);
        subscription.setSubscriber(subscriber);
        entityManager.persist(subscription);
        return subscription;
    }

    public List<User> findAuthorsBySubscriber(User subscriber) {
        String query = "SELECT s.author FROM Subscription s WHERE s.subscriber = :subscriber";
        return entityManager.createQuery(query, User.class)
                .setParameter("subscriber", subscriber)
                .getResultList();
    }

}
