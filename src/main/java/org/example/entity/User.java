package org.example.entity;

import jakarta.persistence.*;
import util.PasswordUtil;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;


    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    // Конструкторы
    public User() {}

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String rawPassword) {
        this.password = PasswordUtil.hashPassword(rawPassword);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


//    public List<Post> getPosts() {
//        return posts;
//    }
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//    }
//    public List<Subscription> getSubscriptions() {
//        return subscriptions;
//    }
//    public void setSubscriptions(List<Subscription> subscriptions) {
//        this.subscriptions = subscriptions;
//    }

}