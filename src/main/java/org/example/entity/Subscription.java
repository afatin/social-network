package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Конструкторы
    public Subscription() {
    }

    public Subscription(User subscriber, User author) {
        this.subscriber = subscriber;
        this.author = author;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    // Переопределение toString()
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriber=" + subscriber.getName() +
                ", author=" + author.getName() +
                '}';
    }
}