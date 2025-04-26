package org.example.entity;

import jakarta.persistence.*;
import org.example.security.Role;
import org.example.util.PasswordUtil;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() {}

    public Long getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public Role getRole() {return role; }
    public void setRole(Role role) {
        this.role = role;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
}