package org.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriptions_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public Subscription() { }

    public User getSubscriberId() {
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

    public Object getId() {
        return id;
    }
}